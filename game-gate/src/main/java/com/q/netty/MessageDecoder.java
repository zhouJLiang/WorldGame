package com.q.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode");
        //数据不够时候 暂时不处理,等数据继续来了之后再处理
        if (in.readableBytes() < 8) {
            return;
        }
        //剔除标记位
        in.markReaderIndex();
        in.readInt();
        //获取消息体得真实长度  content
        int length = in.readInt();
        //消息体太大了  为啥是128K? jvm默认得栈帧大小就是128K 大了 就Stack Overflow
        if (length < 8 || length > 128 * 1024) {
            throw new IOException("length error");
        }
        byte[] message = new byte[length];
        in.readBytes(message);
        out.add(message);
    }
}
