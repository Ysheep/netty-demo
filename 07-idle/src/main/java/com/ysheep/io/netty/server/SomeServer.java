package com.ysheep.io.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * netty 是 用于底层通信的
 *  {@link IdleStateHandler }读写空闲检测
 */
public class SomeServer {
    public static void main(String[] args) throws InterruptedException {
        // Server的parentGroup 处理与客户端的连接请求
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 与客户端的channel绑定，用于处理该Channel中的所有请求的操作
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    //用于指定要创建channel的类型
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取channel中的Pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加一个基于行的解码器
                            pipeline.addLast(new LineBasedFrameDecoder(2048));
                            // 在3秒内没有发生读操作就触发读操作空闲操作
                            // 在5秒内没有发生写操作就触发写操作空闲操作

//                            pipeline.addLast(new IdleStateHandler(3,5,0));

                            //在5秒内，即发生了读也发生了写，才不会发生这个超时操作
                            //如果在5秒内，有读或写其中一个没有发生的时候，就会发生这个超时操作。
                            pipeline.addLast(new IdleStateHandler(0, 0, 5));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new SomeServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("服务器已启动");
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
