package com.ysheep.io.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description 自定义处理器初始化器
 * 当前类的实例在pipeline初始化完毕后就会被GC，所以一般这种实现方法不推荐。
 * @createTime 2020/06/07/  16:05:00
 */
public class SomeChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        // 将自定义的handler 放到 channelPipeline中
        // 按照添加到channelPipeline中的顺序处理channel的数据
        pipeline.addLast(new SomeServerHandler());
    }
}
