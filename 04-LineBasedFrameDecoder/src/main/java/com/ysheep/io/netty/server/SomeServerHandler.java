package com.ysheep.io.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/06/08/  22:18:00
 */
public class SomeServerHandler extends SimpleChannelInboundHandler<String> {

    int counter = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("server端收到的第【" + ++counter + "】数据包" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
