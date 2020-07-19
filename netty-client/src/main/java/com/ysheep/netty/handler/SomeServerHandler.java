package com.ysheep.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/06/07/  22:16:00
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将来自客户端的数据显示在服务端的控制台
        System.out.println(ctx.channel().remoteAddress() + "," + msg);
        //给客户端发送数据
//        ctx.channel().writeAndFlush("from server" + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
