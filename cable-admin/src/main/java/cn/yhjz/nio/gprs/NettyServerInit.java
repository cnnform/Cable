package cn.yhjz.nio.gprs;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Netty 初始化
 *
 * @author mackjava
 */
@Component
@Slf4j
public class NettyServerInit {

    @Value("${server.nio.gprs.port}")
    private int port;

    @Autowired
    private ApplicationContext ctx;

    /**
     * 线程开启监听
     */
    @PostConstruct
    public void lister() {
        new Thread(()->{
            init();
        }).start();
    }

    /**
     * 初始化
     */
    private void init() {
        log.info("开始启动定位设备netty服务端");
        //主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //工作线程组
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();

//                        pipeline.addLast(new LineBasedFrameDecoder(1024));

//                        pipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast("decoder",new MyDecoder());
                        //pipeline.addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast("encoder",new MyEncoder());
                        pipeline.addLast(ctx.getBean(NettyServer.class));
                    }
                })
                // 设置队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        // 绑定端口,开始接收进来的连接
        try {
            ChannelFuture cf = bootstrap.bind(port).sync();
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("服务启动监听器异常: " + e.getMessage());
        } finally {
            //关闭主线程组
            bossGroup.shutdownGracefully();
            //关闭工作线程组
            workGroup.shutdownGracefully();
        }
    }

}
