package com.ysheep.netty.server;

import com.ysheep.netty.handler.SomeServerHandler;
import com.ysheep.nettyclient.handler.SomeClientHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/06/07/  22:05:00
 */
public class SomeServer {

    public static void main(String[] args) throws Exception {
        // 用于处理客户端连接请求，将请求发送给childGroup中的eventLoop
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 用于处理客户端请求
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 将channel中的ByteBuf转为String
                            pipeline.addLast(new IdleStateHandler(0, 0, 5));
                            pipeline.addLast(new StringDecoder());
                            // 字符串编码,将String编码为将要发送到channel中的ByteBuf
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new SomeServerHandler());

                        }
                    });

            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("服务端启动成功，端口号:" + "8888");
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }


}
