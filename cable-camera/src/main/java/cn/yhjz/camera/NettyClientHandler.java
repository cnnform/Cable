package cn.yhjz.camera;

import cn.yhjz.camera.cache.CameraHolder;
import cn.yhjz.camera.entity.AbstractCamera;
import cn.yhjz.camera.entity.Camera;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public static ChannelHandlerContext ctx;

    public static String NETWORK_AREA_CODE;

    public static Map<String, PreviewThread> cameraPreviewThreadMap = new ConcurrentHashMap<>();

    public static String pythonUrl;


    // 当从服务器接收到一条消息时被调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String msgString = msg.toString(CharsetUtil.UTF_8);
        try {
            JSONObject msgJson = JSONObject.parseObject(msgString);
            String orderType = msgJson.getString("orderType");
            JSONObject bodyJson = msgJson.getJSONObject("body");
            JSONObject extraJson = msgJson.getJSONObject("extra");
            Long cameraId = bodyJson.getLong("cameraId");
            if ("start_preview".equals(orderType)) {
                log.info("收到start_preview");
                Thread.sleep(1000);
                //开启预览功能
                previewCamera(cameraId);
            }
            if ("stop_preview".equals(orderType)) {
                log.info("stop_preview");
                //停止预览的线程
                PreviewThread previewThread = cameraPreviewThreadMap.get(cameraId.toString());
                if (previewThread != null) {
                    previewThread.setPreviewing(false);
                }
            }
            if ("get_ptz_control".equals(orderType)) {
                log.info("get_ptz_control");
            }
            if ("set_ptz_control".equals(orderType)) {
                log.info("set_ptz_control");
                // 设置云台PTZ位置
                Double wPanPos = bodyJson.getDouble("wPanPos");
                Double wTiltPos = bodyJson.getDouble("wTiltPos");
                Double wZoomPos = bodyJson.getDouble("wZoomPos");
                log.info("设置云台：p:{},t:{},z:{}", wPanPos, wTiltPos, wZoomPos);
                AbstractCamera camera = getCameraById(cameraId);
//                camera.getPTZ();
                camera.setPTZ(wPanPos, wTiltPos, wZoomPos);
            }
            if ("capture".equals(orderType)) {
//                log.info("收到capture");
                //获得一张截图
                AbstractCamera camera = getCameraById(cameraId);
                String base64 = camera.capture();
                //把截图发送
                JSONObject toMsgJson = new JSONObject();
                bodyJson.put("cameraId", cameraId);
                bodyJson.put("imgBase64", base64);
                toMsgJson.put("orderType", "push_frame");
                toMsgJson.put("body", bodyJson);
                ctx.writeAndFlush(Unpooled.copiedBuffer(toMsgJson.toJSONString(), CharsetUtil.UTF_8));
            }
        } catch (Exception exp) {
            log.error("{}", exp);
            log.error("读取数据异常：{}", msgString);
        }
    }

    /**
     * 在到服务器的连接已建立之后将被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyClientHandler.ctx = ctx;
        //建立连接之后立即发送消息报告自己的区域
        JSONObject msgJson = new JSONObject();
        msgJson.put("orderType", "region");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("networkArea", NETWORK_AREA_CODE);
        msgJson.put("body", bodyJson);
        log.info("推送NETWORK_AREA_CODE");
        ctx.writeAndFlush(Unpooled.copiedBuffer(msgJson.toJSONString(), CharsetUtil.UTF_8));
        //启动一个线程，检查是否获得了摄像头列表
        new Thread(() -> {
            while (null == CameraHolder.cameraList) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("开始登录每个摄像头，摄像头数量：{}", CameraHolder.cameraList.size());
            //存在摄像头列表之后，为每一个摄像头启动一个线程去连接登录
            for (AbstractCamera camera : CameraHolder.cameraList) {
                try {
                    camera.init();
                    camera.startPtzThread();
                } catch (Exception exp) {
                    log.error("摄像头连接失败，cameraId:{}", camera.getId());
                    continue;
                }
                log.info("摄像头初始化完毕，cameraId:{}", camera.getId());
            }
        }).start();
    }

    /**
     * 系统关闭的时候，保证所有注销所有的摄像头
     */
    public void preDestroy() {
        if (!CollectionUtils.isEmpty(CameraHolder.cameraList)) {
            for (AbstractCamera camera : CameraHolder.cameraList) {
                if (camera.getIsLogin()) {
                    camera.close();
                    log.info("摄像头注销完毕，{}", camera.getId());
                }
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.stopAllPreview();
        NettyClientHandler.ctx = null;
    }

    // 在处理过程中引发异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印异常
        cause.printStackTrace();
        // 关闭Channel
        ctx.channel();
        this.stopAllPreview();
        ctx = null;
    }

    /**
     * 停止所有预览的线程。
     */
    private void stopAllPreview() {
        Set<String> keySet = cameraPreviewThreadMap.keySet();
        for (String key : keySet) {
            PreviewThread previewThread = cameraPreviewThreadMap.get(key);
            previewThread.stop0();
            cameraPreviewThreadMap.remove(key);
        }
        ctx = null;
    }

    public void previewCamera(Long cameraId) {
        //启动一个线程预览图像
        //此处不能使用线程池，因为预览线程不能等待
        PreviewThread previewThread = cameraPreviewThreadMap.get(cameraId.toString());
        if (null != previewThread) {
            previewThread.setPreviewing(true);
        }
    }

    public void startCameraThread(Long cameraId) {
        //启动一个线程预览图像
        //此处不能使用线程池，因为预览线程不能等待
        PreviewThread previewThread = new PreviewThread(cameraId, null);
        cameraPreviewThreadMap.put(cameraId.toString(), previewThread);
        previewThread.start();
    }

    @Data
    public static class PreviewThread extends Thread {
        private Long cameraId;
        private JSONObject extraJson;

        private boolean previewing = false;
        /**
         * 控制线程是否运行
         */
        private boolean running = false;

        public PreviewThread(Long cameraId, JSONObject extraJson) {
            this.cameraId = cameraId;
            this.extraJson = extraJson;
        }

        /**
         * 停止预览线程
         */
        public void stop0() {
            this.running = false;
        }

        @Override
        public void run() {
            //根据cameraId获得camera对象
            this.running = true;
            log.info("开始分析摄像头的线程,{}", cameraId);
            List<AbstractCamera> aimCameraList = CameraHolder.cameraList.stream().filter(
                    (c) -> c.getId().equals(cameraId)
            ).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(aimCameraList)) {
                log.error("没找到摄像头：{}", cameraId);
                return;
            }
            AbstractCamera aimCamera = aimCameraList.get(0);

            try {
                log.info("开始获得摄像头图像:{}", cameraId);
                while (this.running && ctx != null) {

                    String imgBase64String = "";
                    JSONObject eventJson = null;
                    try {
                        imgBase64String = aimCamera.capture();
                        JSONObject pythonResultJson = prePython(imgBase64String);
                        eventJson = pythonResultJson.getJSONObject("event");
                        imgBase64String = pythonResultJson.getString("imageBase64");
                    } catch (Exception e) {
                        log.error("获取摄像头图像出现异常，{},{}", cameraId, e);
                        Thread.sleep(1000);
                        continue;
                    }
                    if (!StringUtils.hasText(imgBase64String)) {
                        continue;
                    }
                    JSONObject msgJson = new JSONObject();
                    JSONObject bodyJson = new JSONObject();
                    bodyJson.put("cameraId", cameraId);
                    bodyJson.put("imgBase64", imgBase64String);
                    msgJson.put("orderType", "push_frame");
                    if (previewing) {
                        bodyJson.put("event", eventJson);
                        msgJson.put("body", bodyJson);
                        msgJson.put("extraJson", extraJson);
                        ctx.writeAndFlush(Unpooled.copiedBuffer(msgJson.toJSONString(), CharsetUtil.UTF_8));
                    } else if (eventJson != null) {
                        bodyJson.put("event", eventJson);
                        msgJson.put("body", bodyJson);
                        msgJson.put("extraJson", extraJson);
                        ctx.writeAndFlush(Unpooled.copiedBuffer(msgJson.toJSONString(), CharsetUtil.UTF_8));
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                log.error("{}", e);
            } catch (Exception e) {
                log.error("{}", e);
            }

        }
    }

    public static JSONObject prePython(String base64Str) {
        OkHttpClient httpClient = new OkHttpClient();
        JSONObject paramJson = new JSONObject();
        paramJson.put("img_str", base64Str);
        //同时传递报警区域
        //我们假设4个边，点的坐标用百分比表示，左上角为原点
        //80，90，10，20
        JSONObject edgeJson = new JSONObject();
        edgeJson.put("top", 50);
        edgeJson.put("bottom", 100);
        edgeJson.put("left", 10);
        edgeJson.put("right", 50);
        paramJson.put("edge", edgeJson);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                paramJson.toJSONString());
        Request request = new Request.Builder()
                .url(NettyClientHandler.pythonUrl).post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            JSONObject responseJson = JSONObject.parseObject(response.body().string());
            return responseJson;
        } catch (IOException e) {
            log.error("请求");
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前的Camera对象
     */
    public AbstractCamera getCameraById(Long cameraId) {
        List<AbstractCamera> aimCameraList = CameraHolder.cameraList.stream().filter(
                (c) -> c.getId().equals(cameraId)
        ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(aimCameraList)) {
            log.error("没找到摄像头：{}", cameraId);
            return null;
        }
        AbstractCamera aimCamera = aimCameraList.get(0);
        return aimCamera;
    }

}