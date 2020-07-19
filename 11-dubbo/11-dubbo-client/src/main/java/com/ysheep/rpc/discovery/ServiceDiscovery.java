package com.ysheep.rpc.discovery;

import java.util.List;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/16
 */
public interface ServiceDiscovery {
    /**
     * @param serviceName
     * @return 返回服务列表
     */
    List<String> discovery(String serviceName) throws Exception;
}
