package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/06/08/  22:15:00
 */
public class SomeServer {

    public static void main(String[] args) throws Exception{

        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup chilGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, chilGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 将channel中的ByteBuf转为String
                            pipeline.addLast(new StringDecoder());
                            // 字符串编码,将String编码为将要发送到channel中的ByteBuf
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new SomeServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(8888).sync();
            System.out.println("服务端启动成功，端口号:" + "8888");
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            chilGroup.shutdownGracefully();

        }


    }
}
