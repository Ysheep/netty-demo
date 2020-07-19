package com.ysheep.rpc.discovery;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/16
 */
public class ServerDiscoveryTest {

    public static void main(String[] args) throws Exception {

        System.out.println(new ServiceDiscoveryImpl().discovery("com.ysheep.rpc.api.RpcSomeApi"));
    }
}
