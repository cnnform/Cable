package cn.yhjz.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/video/{cid}")
@Slf4j
public class VideoWsServerEndpoint {
    private Session session;
    private String cid;
    public static ConcurrentHashMap<String, VideoWsServerEndpoint> webSocketMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("cid") String cid) {
        this.session = session;
        webSocketMap.put(cid, this);
        this.cid = cid;
        log.info("Session: " + session.getId() + ", 新窗口开始监听:" + cid);
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(this.cid);
    }

    @OnMessage
    public void onMessage(String data, Session session) {

    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(ByteBuffer message) throws IOException {
        if (null != message && message.hasArray()) {
            message.flip();
            this.session.getBasicRemote().sendBinary(message);
        }
    }
}
