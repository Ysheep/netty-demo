package com.ysheep.rpc.service.impl;

import com.ysheep.rpc.api.RpcSomeApi;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/12/  06:57:00
 */
public class RpcSomeApiImpl implements RpcSomeApi {
    @Override
    public String hello(String name) {
        return "hi: " + name;
    }

    @Override
    public String say(String name, String msg) {
        return name + "say : " + msg;
    }
}
