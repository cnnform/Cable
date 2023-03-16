package cn.yhjz.biz.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CameraMqttCommander {

    @Autowired
    private MqttGateway mqttGateway;

    public void setPtzControl(JSONObject jsonData) {
        JSONObject body = jsonData.getJSONObject("body");
        String networkArea = body.getString("networkArea");
        long wPanPos = body.getLong("wPanPos");
        long wTiltPos = body.getLong("wTiltPos");
        // 垂直区间: [-1, 1], 度数: 90 ~ 0
        // 水平区间: [-1, 1], 度数: 180~ 270 ~ 0 ~ 180
        double ps;
        if (wPanPos > 180) {
            ps = -(360.0 - wPanPos) / 180;
        } else {
            ps = wPanPos / 180.0;
        }
        log.info("目标角度P:{}", ps);
        double ts = wTiltPos * (1.0 / 45);
        ts = 1 - ts;
        body.put("wPanPos", ps);
        body.put("wTiltPos", ts);
        String deviceId = body.getString("deviceId");
        mqttGateway.sendToMqtt("cable_set_ptz/" + deviceId, 1, body.toJSONString());
        log.info("发送主题：{}", "cable_set_ptz/" + deviceId);
    }

    public void sendWebrtcOffer(JSONObject jsonData) {
        JSONObject body = jsonData.getJSONObject("body");
        String deviceId = body.getString("deviceId");
        mqttGateway.sendToMqtt("cable_webrtc_offer/" + deviceId, 2, body.toJSONString());
    }

    /**
     * 发送停止webrtc的指令
     *
     * @param jsonData
     */
    public void sendWebrtcStop(JSONObject jsonData) {
        JSONObject body = jsonData.getJSONObject("body");
        String deviceId = body.getString("deviceId");
        mqttGateway.sendToMqtt("stop_webrtc/" + deviceId, 2, body.toJSONString());
    }

    /**
     * 发送启动目标检测的指令
     *
     * @param jsonData
     */
    public void sendEnableDetect(JSONObject jsonData) {
        JSONObject body = jsonData.getJSONObject("body");
        String deviceId = body.getString("deviceId");
        mqttGateway.sendToMqtt("cable_webrtc_enable_detect/" + deviceId, 2, body.toJSONString());
    }

    /**
     * 发送停止目标检测的指令
     *
     * @param jsonData
     */
    public void sendDisableDetect(JSONObject jsonData) {
        JSONObject body = jsonData.getJSONObject("body");
        String deviceId = body.getString("deviceId");
        mqttGateway.sendToMqtt("cable_webrtc_disable_detect/" + deviceId, 2, body.toJSONString());
    }
}
