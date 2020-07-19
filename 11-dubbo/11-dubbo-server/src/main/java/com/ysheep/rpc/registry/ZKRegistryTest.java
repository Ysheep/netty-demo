package com.ysheep.rpc.registry;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/14
 */
public class ZKRegistryTest {

    public static void main(String[] args) throws Exception{
        ZKRegistry registry = new ZKRegistry();
        registry.register("com.ysheep.SomService","localhost:8888");
        System.in.read();
    }
}
