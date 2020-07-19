package com.ysheep.rpc.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/16
 */
public class RandonLoadBalance implements LoadBalance {

    @Override
    public String choose(List<String> servers) {
        if (servers == null) {
            return null;
        }
        if (servers.size() == 1) {
            return servers.get(0);
        }
        int index = new Random().nextInt(servers.size());
        return servers.get(index);
    }
}
