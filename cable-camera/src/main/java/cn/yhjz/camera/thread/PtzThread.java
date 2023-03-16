package cn.yhjz.camera.thread;

import cn.yhjz.camera.NettyClientHandler;
import cn.yhjz.camera.entity.AbstractCamera;
import cn.yhjz.camera.mqtt.MqttGateway;
import cn.yhjz.camera.mqtt.MqttUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class PtzThread extends Thread {

    private AbstractCamera camera;

    public PtzThread(AbstractCamera camera) {
        this.camera = camera;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                if (this.camera.getIsOnline()) {
                    JSONObject ptzJson = this.camera.getPTZ();
                    JSONObject sendJson = new JSONObject();
                    sendJson.put("orderType", "get_ptz_control");
                    JSONObject bodyJson = new JSONObject();
                    bodyJson.put("cameraId", this.camera.getId());
                    bodyJson.put("wPanPos", ptzJson.get("wPanPos"));
                    bodyJson.put("wTiltPos", ptzJson.get("wTiltPos"));
                    bodyJson.put("wZoomPos", ptzJson.get("wZoomPos"));
                    sendJson.put("body", bodyJson);
                    if (null == NettyClientHandler.ctx) {
                        break;
                    }
                    String body = sendJson.toJSONString();
//                    log.info("发送ptz");
//                    NettyClientHandler.ctx.writeAndFlush(Unpooled.copiedBuffer(body, CharsetUtil.UTF_8));
                    //通过mqtt发送一次
                    MqttUtil.mqttGateway.sendToMqtt("cable_push_up_ptz", 0, body);
                } else {
                    //如果摄像头不在线了，直接退出此线程
                    break;
                }
            } catch (InterruptedException e) {
                log.error("获得ptz出现异常-->{}", e);
            }
        }
    }
}
