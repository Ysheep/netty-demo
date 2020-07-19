package com.ysheep.io.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * {@link LineBasedFrameDecoder}基于行的帧解码器，即会按照行分隔符对数据进行拆包粘包，解码出 ByteBuf
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/06/08/  22:15:00
 */
public class SomeServer {

    public static void main(String[] args) throws Exception{

        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 基于行的接码器，可以让发送的数据正确一行，但是如果超过了一行的长度，接收就会报异常。
                            pipeline.addLast(new LineBasedFrameDecoder(5031));
                            // 字符串编码,将String编码为将要发送到channel中的ByteBuf
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new SomeServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(8888).sync();
            System.out.println("服务端启动成功，端口号:" + "8888");
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();

        }


    }
}
