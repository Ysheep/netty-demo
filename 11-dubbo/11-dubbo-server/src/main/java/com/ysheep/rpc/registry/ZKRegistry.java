package com.ysheep.rpc.registry;

import com.ysheep.rpc.constant.ZKConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/14
 */
public class ZKRegistry implements RegistryCenter {

    private CuratorFramework curator;

    public ZKRegistry() {
        this.curator = CuratorFrameworkFactory.builder()
                //连接zk集群
                .connectString(ZKConstant.ZK_CLUSTER)
                //连接超时时间
                .connectionTimeoutMs(10000)
                //回话超时时间
                .sessionTimeoutMs(40000)
                //指定重试机制，每重试一次，休眠1秒。最多重试10次
                .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        //zk客户端启动
        curator.start();

    }


    public void register(String serviceName, String serviceAddr) throws Exception{
        //将要创建的持久节点路径
        String servicePath = ZKConstant.ZK_DUBBO_ROOT_PATH + "/" + serviceName;
        // 1、创建服务名称的持久节点
        if (curator.checkExists().forPath(servicePath) == null) {
            curator.create()
                    // 若父节点不存在，则创建父节点
                    .creatingParentsIfNeeded()
                    // 指定创建持久节点
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(servicePath);
        }

        // 2、创建ip:port的临时节点
        String addressPath = servicePath + "/" + serviceAddr;
        if (curator.checkExists().forPath(addressPath) == null) {
            curator.create()
                    //创建临时节点
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath);
        }
    }
}
