package com.ysheep.io.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/05/  17:06:00
 */
public class SomeServerHandler extends ChannelInboundHandlerAdapter {

    /**用户时间触发*/
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventDesc = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventDesc = "读空闲超时";
                    break;
                case WRITER_IDLE:
                    eventDesc = "写空闲超时";
                    break;
                case ALL_IDLE:
                    eventDesc = "读和写都超时";
            }
            System.out.println("IdleStateEvent" + eventDesc);
//            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
