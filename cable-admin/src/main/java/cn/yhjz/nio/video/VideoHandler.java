package cn.yhjz.nio.video;

import cn.yhjz.websocket.VideoWsServerEndpoint;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class VideoHandler extends SimpleChannelInboundHandler<Object> {

    private static ConcurrentHashMap<String, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> ctxIdAndCid = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        String ctxId = ctx.channel().id().asShortText();
        if (o instanceof DefaultHttpContent) {
            DefaultHttpContent content = (DefaultHttpContent) o;
            String cid = ctxIdAndCid.get(ctxId);
            VideoWsServerEndpoint wsServerEndpoint = VideoWsServerEndpoint.webSocketMap.get(cid);
            if (null != wsServerEndpoint) {
                int readableBytes = content.content().readableBytes();
                byte[] contentBytes = new byte[readableBytes];
                content.content().readBytes(contentBytes);
                ByteBuffer byteBuffer = ByteBuffer.allocate(readableBytes);
                byteBuffer.put(contentBytes);
//                log.info("{}",Thread.currentThread().getId());
                try {
                    wsServerEndpoint.sendMessage(byteBuffer);
                } catch (IllegalStateException exception) {
                    log.info("连接已关闭，放弃此包");
                }
            }
        }
        if (o instanceof DefaultHttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) o;
            log.info("{}", request.uri());
            String uri = request.uri();
            if (uri.startsWith("/push")) {
                String cid = uri.replace("/push/", "");
                ctxIdAndCid.put(ctxId, cid);
                log.info("接到ffmpeg的推送，{}",cid);
            } else {
                ctx.close();
            }
        }
//        ByteBuf data = (ByteBuf) o;
//        int all = data.readableBytes();
//        byte[] dataBytes = new byte[all];
//        data.readBytes(dataBytes);
//        log.info("收到数据量{}", all);
    }
}
