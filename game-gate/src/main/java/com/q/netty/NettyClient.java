package com.q.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyClient {
    private String host;
    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public  void  start(){
        System.out.println("客户端连接启动");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();//连接到远程节点,阻塞等待直到连接完成
            future.channel().closeFuture().sync();//阻塞 直到channel关闭
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();//关闭线程池并且释放所有资源
        }
    }

    public static void main(String[] args) {
        new NettyClient("127.0.0.1",1122).start();
    }
}

