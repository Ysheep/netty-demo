package com.ysheep.io.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/06/08/  22:06:00
 */
public class SomeClienthandler extends ChannelInboundHandlerAdapter {
    private String message = "Hello Word";


    /**
     * 启动时主动向server发送请求
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] bytes = message.getBytes();
        ByteBuf buf = null;
        for (int i = 0; i < 100; i++) {

            // 申请缓存空间
            buf = Unpooled.buffer(bytes.length);
            //将数据写入到缓存
            buf.writeBytes(bytes);
            ctx.writeAndFlush(buf);

        }

//        ctx.channel().writeAndFlush(message);
//        ctx.channel().writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
