package com.ysheep.io.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/05/  17:06:00
 */
public class SomeServerHandler extends ChannelInboundHandlerAdapter {
    // 创建一个ChannelGroup,其是一个线程安全的集合，其中存放着与当前服务器相连接的所有active的channel
    private static ChannelGroup groups = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        // 给所有的消息广播给素有group中的客户端channel
        groups.forEach(ch -> {
            if (ch != channel) {
                ch.writeAndFlush("from " + channel.remoteAddress() + "发送的:" + msg + "\n");
            } else {
                channel.writeAndFlush("from ME 发送的:" + msg + "\n");
            }
        });
    }

    // 只要有客户端channel与服务端连接成功就触发这个方法的执行
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "---上线了");
        groups.writeAndFlush(channel.remoteAddress() + " ----上线了\n");
        groups.add(ctx.channel());

    }

    // 只要有客户端channel与服务端断开连接，就会执行这个方法
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "下线的");
        groups.writeAndFlush(channel.remoteAddress() + "---下线了，当前在线人数"+ groups.size() + "\n");
        // groups中存放的都是active状态的channel，一旦某一个channel的状态不在是active的状态呢，groups会自动移除。所以不用调用remove
        //        groups.remove(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
