package cn.yhjz.websocket;

import cn.yhjz.biz.config.CameraMqttCommander;
import cn.yhjz.biz.controller.BizVideoCamera;
import cn.yhjz.nio.camera.CameraMessageHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * webservice服务端，此类使用@ServerEndpoint，是多例，不是单例
 *
 * @author yhjz
 */
@Component
@ServerEndpoint("/websocket/monitor/{sid}")
@Slf4j
public class WsServerEndpoint {

    /**
     * 用来记录当前在线连接数
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<WsServerEndpoint> webSocketSet = new CopyOnWriteArraySet<WsServerEndpoint>();

    /**
     * 某个客户端的连接会话, 需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收token
     */
    private String sid = "";

    private static CameraMqttCommander mqttCommander;

    @Autowired
    private void setCameraMqttCommander(CameraMqttCommander cameraMqttCommander) {
        mqttCommander = cameraMqttCommander;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        this.sid = sid;
        //在线数
        addOnlineCount();
        log.info("Session: " + session.getId() + ", 新窗口开始监听:" + sid + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        CameraMessageHandler.stopPreviewByTry(this);
        // 从set中删除
        webSocketSet.remove(this);
        //在线数减1
        subOnlineCount();
        //断开连接情况下，更新主板占用情况为释放
        log.info("释放的sid为：" + sid);
        //这里写你 释放的时候，要处理的业务
        log.info("连接关闭！当前在线人数为" + getOnlineCount());

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String data, Session session) {
//        CameraWsHandler.send(data, this);
        JSONObject jsonData = JSON.parseObject(data);
        String orderType = jsonData.getString("orderType");
        CameraMessageHandler cameraAnticipation = new CameraMessageHandler();
        if ("start_preview".equals(orderType)) {
            cameraAnticipation.startPreview(jsonData, this);
        } else if ("stop_preview".equals(orderType)) {
            cameraAnticipation.stopPreviewByWs(jsonData, this);
        } else if ("set_ptz_control".equals(orderType)) {
//            cameraAnticipation.setPtzControl(jsonData);
            mqttCommander.setPtzControl(jsonData);
        }else if("webrtc_push_offer".equals(orderType)){
            //页面webrtc专备好了offer，通过mqtt发送到python中
            mqttCommander.sendWebrtcOffer(jsonData);
        }else if("stop_webrtc".equals(orderType)){
            //页面webrtc专备好了offer，通过mqtt发送到python中
            mqttCommander.sendWebrtcStop(jsonData);
        }else if("enable_detect".equals(orderType)){
            //页面webrtc专备好了offer，通过mqtt发送到python中
            mqttCommander.sendEnableDetect(jsonData);
        }else if("disable_detect".equals(orderType)){
            //页面webrtc专备好了offer，通过mqtt发送到python中
            mqttCommander.sendDisableDetect(jsonData);
        }

    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(@PathParam("sid") String sid, String message) {
        log.info("推送消息到窗口" + sid + ",推送内容:" + message);
        for (WsServerEndpoint item : webSocketSet) {
            try {
                if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WsServerEndpoint.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WsServerEndpoint.onlineCount--;
    }

    public static CopyOnWriteArraySet<WsServerEndpoint> getWebSocketSet() {
        return webSocketSet;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
