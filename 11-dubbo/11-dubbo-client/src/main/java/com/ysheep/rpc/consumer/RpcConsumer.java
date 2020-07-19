package com.ysheep.rpc.consumer;

import com.ysheep.rpc.client.RPCProxy;
import com.ysheep.rpc.api.RpcSomeApi;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/11/  22:32:00
 */
public class RpcConsumer {

    public static void main(String[] args) {
      // 1、 获取业务接口
        RpcSomeApi api = RPCProxy.create(RpcSomeApi.class);
        System.out.println(api.hello("rpc-consumer"));
        System.out.println(api.say("rpc-consumer ","hello"));
        System.out.println(api.hashCode());
    }
}
