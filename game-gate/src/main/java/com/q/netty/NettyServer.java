package com.q.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/*
https://www.jianshu.com/nb/7269354；
 1.EventLoopGroup 管理EventLoop 组
 EventExecutor[] children 保存EventExector数组,保存eventLoop（其中每个eventLoop 中都包含taskQueue,selector 是做啥？？ --开始只会使用一个eventLoop运行）;
 EventExecutorChooser chooser 从children中选取一个eventloop策略；两种策略：被2整除，和取模
 2.EventLoop:
 state:线程的状态
 taskQueue:存放任务队列；  任务队列里面放什么？

 thread:线程池维护的唯一线程
 初始化一个线程，并在内部执行eventLoop的run 放法，执行polltaskQueue里面的对象；
3.bind():
 Future:获取一个通信的数据结果，Promise是做数据过程中的数据保证；
 NioServerSocketChannel:封装了ServerSocketChannel 和 keys

 */

public class NettyServer {

    private int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        NettyServer server = new NettyServer(1122);
        server.run();
    }

    public void run() {
        /**NioEventLoopGroup 处理io操作；bossGroup 接受incoming connection;
         WorkGroup 负责处理bossGroup  接受和注册的connection； EventLoopGroup 说白了，
         就是一个死循环，不停地检测IO事件，处理IO事件，执行任务
         group(bossGroup, workerGroup) 我们需要两种类型的人干活，
         一个是老板（可能是多个老板一起去接活），一个是工人（可能是多个工人一起开干），老板负责从外面接活，接到的活分配给工人干，
         放到这里，bossGroup的作用就是不断地accept到新的连接，将新的连接丢给workerGroup来处理
         **/
        //accept 客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理客户端数据的 读写操作
        EventLoopGroup workGroup = new NioEventLoopGroup();
        // NioEventLoop eventLoop = new NioEventLoop();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    //NioServerSocketChannel 用来实例化一个新的channel 来接受进来的connection
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress("127.0.0.1", port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //addlast 类似于过滤器的作用,内部是一个双向链表,在channel处理的时候,根据链表一个个的处理
                            //消息进来,读取 inbound下的所有实现
                            //消息出去,读取直到onbound进行处理
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                            socketChannel.pipeline().addLast(new MessageDecoder());
                            socketChannel.pipeline().addLast(new MessageEncoder());
                        }
                    })
                    //下面两个是option是TCP/IP 中设置的；option() is for the NioServerSocketChannel that accept incoming connection;
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //连接服务器.异步绑定服务器;调用sync()方法阻塞等待直到绑定完成;
            ChannelFuture future = bootstrap.bind().sync();
            //获取channel的closeFuture,并且阻塞当前线程直到绑定完成;
            future.channel().closeFuture().sync();
            System.out.println("netty server has been open!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
/*  protoBUff  websocket 消息解析
 //           HTTP请求的解码和编码
	            pipeline.addLast(new HttpServerCodec());
	            // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse，
	            // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
	            pipeline.addLast(new HttpObjectAggregator(Constants.ImserverConfig.MAX_AGGREGATED_CONTENT_LENGTH));
	            // 主要用于处理大数据流，比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的; 增加之后就不用考虑这个问题了
	            pipeline.addLast(new ChunkedWriteHandler());
	            // WebSocket数据压缩
	            pipeline.addLast(new WebSocketServerCompressionHandler());
	            // 协议包长度限制
	            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, Constants.ImserverConfig.MAX_FRAME_LENGTH));
	            // 协议包解码
	            pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
	                @Override
	                protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> objs) throws Exception {
	                    ByteBuf buf = ((BinaryWebSocketFrame) frame).content();
	                    objs.add(buf);
	                    buf.retain();
	                }
	            });
	            // 协议包编码
	            pipeline.addLast(new MessageToMessageEncoder<MessageLiteOrBuilder>() {
	                @Override
	                protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
	                    ByteBuf result = null;
	                    if (msg instanceof MessageLite) {
	                        result = wrappedBuffer(((MessageLite) msg).toByteArray());
	                    }
	                    if (msg instanceof MessageLite.Builder) {
	                        result = wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
	                    }
	                    // 然后下面再转成websocket二进制流，因为客户端不能直接解析protobuf编码生成的
	                    WebSocketFrame frame = new BinaryWebSocketFrame(result);
	                    out.add(frame);
	                }
	            });
	            // 协议包解码时指定Protobuf字节数实例化为CommonProtocol类型
	            pipeline.addLast(decoder);
	            pipeline.addLast(new IdleStateHandler(Constants.ImserverConfig.READ_IDLE_TIME,Constants.ImserverConfig.WRITE_IDLE_TIME,0));
	            // 业务处理器
*/

  /*

     //ChannelPipeline pipeline = ch.pipeline();
             pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                     pipeline.addLast("decoder", decoder);
                     pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                     pipeline.addLast("encoder",encoder);
                     pipeline.addLast(new IdleStateHandler(Constants.ImserverConfig.READ_IDLE_TIME,Constants.ImserverConfig.WRITE_IDLE_TIME,0));
                     pipeline.addLast("handler", new ImServerHandler(proxy,connertor));
                     }
    */