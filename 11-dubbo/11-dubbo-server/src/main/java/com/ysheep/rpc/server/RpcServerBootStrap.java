package com.ysheep.rpc.server;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description 启动类
 * @createTime 2020/07/11/  22:27:00
 * rpc的步骤
 * 1、服务发布，将对应的服务信息放在注册表中
 * 2、获取调用方的信息，根据服务名获取调用的服务信息
 * 3、在服务端运行服务，返回运行接口
 *
 * consumer
 * 1、创建Consumer的proxy
 * 2、在proxy中调用rpc-server,并返回调用结果
 *
 */
public class RpcServerBootStrap {

    public static void main(String[] args) throws Exception {
        String providerPackage ="com.ysheep.rpc.service";
        RpcSomerServer server = new RpcSomerServer();
        server.publish(providerPackage);
        server.start();
    }
}
