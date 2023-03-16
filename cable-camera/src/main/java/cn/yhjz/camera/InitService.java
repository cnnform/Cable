package cn.yhjz.camera;

import cn.yhjz.camera.cache.CameraHolder;
import cn.yhjz.camera.entity.AbstractCamera;
import cn.yhjz.camera.entity.Camera;
import cn.yhjz.camera.entity.OnvifCamera;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工程启动的时候根据环境变量中设置的网络区域code获得摄像头列表
 */
@Component
@Slf4j
public class InitService {

    @Value("${cable.admin-url}")
    private String url;

    @Value("${cable.admin-server-hostname}")
    private String adminNettyHost;

    @Value("${cable.admin-server-port}")
    private String adminNettyPort;

    public static Integer RES_SUCCESS_CODE = Integer.valueOf(200);

    private OkHttpClient httpClient = new OkHttpClient();

    @Value("${cable.python_url}")
    private String pythonUrl;

    /**
     * 通过http请求获得一个网络区域下的所有摄像头
     */
    @PostConstruct
    public void init() {
        String apiUrl = url + "?code=" + NettyClientHandler.NETWORK_AREA_CODE;
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();
        log.info("管理系统地址：{}", apiUrl);
        try (Response response = httpClient.newCall(request).execute()) {
            String resString = response.body().string();
            JSONObject resJson = JSON.parseObject(resString);
            Integer resCode = resJson.getInteger("code");
            if (!RES_SUCCESS_CODE.equals(resCode)) {
                throw new RuntimeException("请求管理系统失败");
            }
            String resMsg = resJson.getString("msg");
            JSONArray resList = resJson.getJSONArray("data");
            List<Camera> cameraList = resList.toJavaList(Camera.class);

            List<AbstractCamera> abstractCameraList = cameraList.stream().map(camera -> {
                //TODO:根据类型转换成特定的对象，而不是下面这样直接使用onvifcamera
                AbstractCamera ac = new OnvifCamera();
                BeanUtils.copyProperties(camera, ac);
                return ac;
            }).collect(Collectors.toList());
            log.info("{}", abstractCameraList);
            CameraHolder.cameraList = abstractCameraList;
        } catch (IOException e) {
            log.error("请求");
            throw new RuntimeException(e);
        }
    }

    /**
     * 连接管理系统的服务端口，建立长连接。如果不成功等待5秒就重新连接
     */
    @PostConstruct
    public void connectAdmin() {
        //连接管理系统的netty
        new Thread(() -> {
            while (true) {
                log.info("开始连接{}:{}", adminNettyHost, adminNettyPort);
                NioEventLoopGroup group = new NioEventLoopGroup();
                try {
                    // 启动对象
                    Bootstrap bootstrap = new Bootstrap();
                    // 配置
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .remoteAddress(new InetSocketAddress(adminNettyHost, Integer.parseInt(adminNettyPort)))
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    NettyClientHandler.pythonUrl = pythonUrl;
                                    ch.pipeline().addLast(new JsonObjectDecoder(1024 * 1024 * 50));
                                    ch.pipeline().addLast(new NettyClientHandler());
                                }
                            });
                    // 连接远程地址，阻塞至连接完成
                    ChannelFuture channelFuture = bootstrap.connect().sync();
                    log.info("连接完成");
                    // 阻塞直到channel关闭
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    log.error("netty出现错误：{}", e);
                } catch (Exception e) {
                    log.error("netty出现错误：{}", e);
                } finally {
                    // 关闭释放资源
                    try {
                        group.shutdownGracefully().sync();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    log.info("与管理端断开，5秒之后重连");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    @PreDestroy
    public void destroy() {
        log.info("系统关闭，开始退出所有的摄像头");
        //断开所有摄像头的连接
        if (!CollectionUtils.isEmpty(CameraHolder.cameraList)) {
            for (AbstractCamera camera : CameraHolder.cameraList) {
                try {
                    camera.close();
                    log.info("{}摄像头注销完毕，{}", camera, camera.getId());
                } catch (Exception exception) {
                    log.error("{}", exception);
                }

            }
        }

    }

}
