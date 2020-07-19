package com.ysheep.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description {@linl SimpleChannelInboundHandler} 会释放message
 * @createTime 2020/07/12/  06:49:00
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<Object> {

    private Object result;

    public Object getResult() {
        return this.result;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // msg是server传递来的远程调用执行接口
        this.result = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx,cause);
        ctx.close();
    }
}
