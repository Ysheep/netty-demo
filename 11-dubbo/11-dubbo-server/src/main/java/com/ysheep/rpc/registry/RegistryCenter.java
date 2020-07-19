package com.ysheep.rpc.registry;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/14
 */
public interface RegistryCenter {

    /**
     * @param serviceName 服务名称
     * @param serviceAddr 服务地址
     */
    void register(String serviceName, String serviceAddr) throws Exception;
}
