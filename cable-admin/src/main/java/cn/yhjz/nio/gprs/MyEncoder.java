package cn.yhjz.nio.gprs;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class MyEncoder extends MessageToMessageEncoder<CharSequence> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> list) throws Exception {
        if (msg.length() != 0) {
            list.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), Charset.forName("GB18030")));
        }
    }
}
