package cn.yhjz.nio.gprs;

import cn.yhjz.nio.gprs.Anticipation;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty服务端处理器
 *
 * @author mackjava
 */
@Component
@Scope("prototype")
@Slf4j
public class NettyServer extends ChannelInboundHandlerAdapter {

    @Autowired
    private Anticipation anticipation;

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static Map<String, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<String, ChannelHandlerContext>(16);

    public static void sendMessage(String key,String data){
        ChannelHandlerContext ctx = ctxMap.get(key);
        ctx.write(data);
        ctx.flush();
        //writeAndFlush在这里没生效，没找到原因
    }

    /**
     * 客户端链接创建完触发
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        ctxMap.put(ctx.channel().id().toString(), ctx);
    }

    /**
     * 移除已经失效的链接
     */
    private void removeChannelMap(ChannelHandlerContext ctx) {
        for(String key :ctxMap.keySet()){
            if(ctxMap.get(key) != null && ctxMap.get(key).equals(ctx)){
                ctxMap.remove(key);
            }
        }
    }

    /**
     * 客户端连接时触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("连接接入");
    }

    /**
     * 客户端发消息时触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object data) {
        log.info("SocketID: " + ctx.channel().id() + ",服务器收到消息: ");
        log.info("{}",data);

        anticipation.init(data);

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
    public void handlerRemoved(ChannelHandlerContext context){
        log.info("连接断开");
        removeChannelMap(context);
    }

    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        cause.printStackTrace();
        if (!channel.isActive()) {

            ctx.close();
        }
    }
}
