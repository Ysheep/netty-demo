package com.ysheep.rpc.server;

import com.ysheep.rpc.dto.InvokeMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * @author ysheep
 * @version 1.0.0
 * {@link SimpleChannelInboundHandler } 这个会自动将msg释放掉
 * {@link io.netty.channel.ChannelInboundHandlerAdapter} 不会释放msg，如果需要用客户端的msg的数据，可以用这个Handler
 * @Description TODO
 * @createTime 2020/07/11/  22:12:00
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<InvokeMessage> {

    private Map<String, Object> registerMap;

    public RpcServerHandler(Map<String, Object> registerMap) {
        this.registerMap = registerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvokeMessage msg) throws Exception {
        Object result = "没有对应的提供者";

        // 判断服务表中是否含有这个要调用的服务
        if (registerMap.containsKey(msg.getClassName())) {
            // 从注册表中者相应的提供者实例
            Object provider = registerMap.get(msg.getClassName());
            result = provider.getClass().getMethod(msg.getMethodName(), msg.getParamTypes())
                    .invoke(provider, msg.getParamValues());
        }
        ctx.writeAndFlush(result);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
