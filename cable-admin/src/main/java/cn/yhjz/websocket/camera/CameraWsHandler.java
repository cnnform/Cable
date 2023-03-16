package cn.yhjz.websocket.camera;

import cn.yhjz.nio.camera.CameraMessageHandler;
import cn.yhjz.websocket.WsServerEndpoint;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author yhjz
 */
public class CameraWsHandler {

    /**
     * camera 处理
     *
     * @param data > 数据
     */
    public static void init(String data, WsServerEndpoint wsServerEndpoint) {
        JSONObject jsonData = JSON.parseObject(data);
        String orderType = jsonData.getString("orderType");
        CameraMessageHandler cameraAnticipation = new CameraMessageHandler();
        if ("start_preview".equals(orderType)) {
            cameraAnticipation.startPreview(jsonData, wsServerEndpoint);
        } else if ("stop_preview".equals(orderType)){
            cameraAnticipation.stopPreviewByWs(jsonData, wsServerEndpoint);
        } else if ("set_ptz_control".equals(orderType)){
            cameraAnticipation.setPtzControl(jsonData);
        }
    }

}
