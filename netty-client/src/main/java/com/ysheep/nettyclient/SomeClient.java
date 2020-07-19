package com.ysheep.nettyclient;

import com.ysheep.nettyclient.handler.SomeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/06/07/  22:24:00
 */
public class SomeClient {

    public static void main(String[] args) throws Exception{
        NioEventLoopGroup child = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(child)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new SomeClientHandler());
                }
            });
            ChannelFuture future = bootstrap.connect("Localhost",8888).sync();
            future.channel().closeFuture().sync();
        } finally {
            child.shutdownGracefully();
        }
    }
}
