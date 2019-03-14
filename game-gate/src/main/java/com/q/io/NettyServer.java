package com.q.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
//https://netty.io/wiki/user-guide-for-4.x.html
public class NettyServer {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        try {
            nettyServer.start(8081);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void start(int port) throws InterruptedException {
        //主从 selector  boss负责外部链接io,  worker负责boss已经连接过,然后注册到worker得io,并进行逻辑处理
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                //init channel accepting incoming connections
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture future = bootstrap.bind(port).sync();

        System.out.println("server start");
        future.channel().closeFuture().sync();
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }
}
