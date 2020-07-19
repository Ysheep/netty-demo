package com.ysheep.rpc.discovery;

import com.ysheep.rpc.constant.ZKConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/16
 */
public class ServiceDiscoveryImpl implements ServiceDiscovery {

    private CuratorFramework curator;

    private List<String> servers;

    public ServiceDiscoveryImpl() {
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

    @Override
    public List<String> discovery(String serviceName) throws Exception{
        String servicePath = ZKConstant.ZK_DUBBO_ROOT_PATH + "/" + serviceName;
        servers = curator.getChildren().forPath(servicePath);
        // 为微服务节点注册watcher监听
        registerWatch(servicePath);
        return servers;
    }

    /**
     * 监听器，服务节点发生变化就及时更新servers
     * @param servicePath
     */
    private void registerWatch(String servicePath) {
        PathChildrenCache cache = new PathChildrenCache(curator, servicePath, true);
        cache.getListenable().addListener((client, event) -> {
            servers = client.getChildren().forPath(servicePath);
        });


    }
}
