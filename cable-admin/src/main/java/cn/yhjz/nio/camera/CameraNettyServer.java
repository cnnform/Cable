package cn.yhjz.nio.camera;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连接所有camera工程的服务端
 *
 * @author mackjava
 */
@Component
@Scope("prototype")
@Slf4j
public class CameraNettyServer extends ChannelInboundHandlerAdapter {

    @Autowired
    private CameraMessageHandler cameraMessageHandler;

    private static Map<String, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>(16);

    /**
     * 发送数据
     */
    public static void sendMessage(String key, String data) {
        ChannelHandlerContext ctx = ctxMap.get(key);
//        log.info("发送一条信息：{}", data);
//        data += "<identification/>";
        ctx.writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8));
    }

    /**
     * 客户端链接创建完触发
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctxMap.put(String.valueOf(ctx.channel().id()), ctx);
    }

    /**
     * 移除已经失效的链接
     */
    private void removeChannelMap(ChannelHandlerContext ctx) {
        // 移除断开连接的netty
        for (String key : CameraMessageHandler.nyChannelMap.keySet()) {
            if (CameraMessageHandler.nyChannelMap.get(key) != null && CameraMessageHandler.nyChannelMap.get(key).equals(ctx.channel().id().toString())) {
                CameraMessageHandler.nyChannelMap.remove(key);
            }
        }

        for (String key : ctxMap.keySet()) {
            if (ctxMap.get(key) != null && ctxMap.get(key).equals(ctx)) {
                ctxMap.remove(key);
            }
        }
    }

    /**
     * 客户端连接时触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("连接接入!");
    }

    /**
     * 客户端发消息时触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object data) {
//        log.info("SocketID: " + ctx.channel().id());
        ByteBuf bodyBuffer = (ByteBuf) data;
        String bodyString = bodyBuffer.toString(Charset.forName("UTF-8"));
        cameraMessageHandler.handle(String.valueOf(ctx.channel().id()), bodyString);
        bodyBuffer.release();
    }

    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    /**
     * 客户端断开连接时调用
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        log.info("连接断开");
        removeChannelMap(context);
    }

    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        log.info("Exception: {}", cause.toString());
        if (!channel.isActive()) {
            ctx.flush();
            ctx.close();
        }
    }
}
