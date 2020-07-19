package com.ysheep.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description 服务的发布注册
 * @createTime 2020/07/11/  21:27:00
 */
public class RpcSomerServer {

    // 定义服务注册表(可以考虑是否有线程安全问题)
    private Map<String, Object> registerMap = new HashMap<>();
    // 用于缓存提供者的类名
    private List<String> classCaches = new ArrayList<>();
    //这是一个线程安全的list
//    private List<String> classCaches = Collections.synchronizedList(new ArrayList<>());


    public void publish(String providerPackage) throws Exception {

        //1、 将指定保重的提供者类名写入缓存classCaches中
        cacheProviderClass(providerPackage);
        //2、进行注册(服务名与提供者实例的映射关系写入注册表中)
        doRegister();

    }

    private void doRegister() throws Exception {

        if (classCaches.isEmpty()) {
            return;
        }
        for (String className : classCaches) {
            Class clzz = Class.forName(className);
            // rpc的服务提供者只能是实现一个接口
            registerMap.put(clzz.getInterfaces()[0].getName(), clzz.newInstance());
        }
    }

    private void cacheProviderClass(String providerPackage) {
        //获取指定包中的资源
        //com.ysheep.rpc.service,将点替换成斜杠/
        URL resource = this.getClass().getClassLoader().getResource(providerPackage.replaceAll("\\.", "/"));
        if (resource == null) {
            return;
        }
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                // 递归查找子目录
                cacheProviderClass(providerPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String fileName = file.getName().replace(".class", "").trim();
                // 将当前文件的全限定性类名写到classCaches中
                classCaches.add(providerPackage + "." + fileName);
            }
        }
        System.out.println("classCaches : " + classCaches);
    }

    // 启动当前Server
    public void start() throws Exception {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    // 用于指定当服务端的请求处理线程全被占用时，
                    // 临时存放已经完成了三次握手的请求的队列的长度，默认是50
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 指定启用心跳机制维护这个长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            // 自定义的Handler
                            pipeline.addLast(new RpcServerHandler(registerMap));
                        }
                    });
            ChannelFuture future = bootstrap.bind(8888).sync();
            System.out.println("微服务注册完成");
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
