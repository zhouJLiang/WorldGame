package com.q.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 放初始任务 比如 前面定义  private  ByteBuf buf;
     * 在 added() 中 进行初始化
     * buf = ctx.alloc().buffer(4);
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive收到客户端的连接");
        ctx.writeAndFlush(Unpooled.copiedBuffer("netty server back rocks!", CharsetUtil.UTF_8));
    }

    /**
     *当收到消息的时候会调用这个方法，消息的最后需要调用release 方法,否则一直的得不到清理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            System.out.println("channelRead开始读取消息：msg:" + msg);
            ctx.writeAndFlush(Unpooled.copiedBuffer("netty server back rocks!", CharsetUtil.UTF_8));
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 消息出现异常调用，当触发的时候相关联的channel会关闭，
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}

