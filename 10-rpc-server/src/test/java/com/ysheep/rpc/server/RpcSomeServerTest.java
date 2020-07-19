package com.ysheep.rpc.server;

import org.junit.Test;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/11/  21:41:00
 */
public class RpcSomeServerTest {

    @Test
    public void testAddClassCaches() throws Exception{
        String providerPackage = "com.ysheep.rpc.service";
        new RpcSomerServer().publish(providerPackage);

    }

}
