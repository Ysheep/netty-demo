package com.ysheep.rpc.loadbalance;

import java.util.List;

/**
 * @author ysheep
 * @version 1.0.0
 * @Description TODO
 * @createTime 2020/07/16
 */
public interface LoadBalance {

    public String choose(List<String> servers);
}
