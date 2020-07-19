package com.ysheep.io.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description 服务器启动类
 * @createTime 2020/06/07/  15:30:00
 */
public class SomeServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();


        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class) //指定使用的Nio通信
//                    .childHandler(new SomeChannelInitializer()); // 这种方式不推荐
                    // 指定childGroup中eventLoopGroup所绑定的线程所要处理的处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            // 放入自定义的处理器Handler
                            pipeline.addLast(new SomeServerHandler());
                        }
                    });
            //指定当前服务器监听的端口号
            // bind()方法的执行时异步的。
            // sync()方法会使bind的方法操作与后续的代码的执行由异步边为了同步
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("服务器启动成功。监听的端口号为：8888");
            // 关闭channel
            // closeFuture的执行是异步的，当chnnel调用了close方法才会触发closeFuture方法才执行
            future.channel().closeFuture().sync();
        }  finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
