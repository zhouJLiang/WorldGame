package com.q.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("encode");
        if (!(msg instanceof byte[])) {
            return;
        }
        byte[] rem = (byte[]) msg;
//        ByteBuf b = ctx.alloc().buffer(0,rem.length);
//        b.writeBytes(rem);
        out.writeBytes(rem);
    }
}
