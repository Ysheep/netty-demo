package com.ysheep.rpc.client;

import com.ysheep.rpc.dto.InvokeMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description 通过动态proxy，调用远程服务端的接口。通过proxy代理调用的接口，而接口的真正实现是在远程服务端。
 * @createTime 2020/07/11/  22:35:00
 */
public class RPCProxy {

    // 泛型方法
    public static <T> T create(Class<?> clzz) {

        return (T) Proxy.newProxyInstance(clzz.getClassLoader(), new Class[]{clzz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 如果目标方法是Object的方法，则直接进行本地调用
                if (Object.class.equals(method.getDeclaringClass())) {
                    // 这里的this 是内部类 InvocationHandler
                    return method.invoke(this, args);
                }
                // 进行远程调用
                return rpcInvoke(clzz,method,args);
            }
        });
    }

    private static Object rpcInvoke(Class clzz,Method method, Object[] args) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        RpcClientHandler handler = new RpcClientHandler();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            // 自定义的Handler
                            pipeline.addLast(handler);
                        }
                    });
            ChannelFuture future = bootstrap.connect("localhost", 8888).sync();
            // 将调用信息发送给server
            future.channel().writeAndFlush(createRequest(clzz, method, args)).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
        return handler.getResult();
    }

    private static InvokeMessage createRequest(Class<?> clzz,Method method, Object[] args) {
        InvokeMessage message = new InvokeMessage();
        message.setClassName(clzz.getName());
        message.setMethodName(method.getName());
        message.setParamTypes(method.getParameterTypes());
        message.setParamValues(args);
        return message;
    }
}
