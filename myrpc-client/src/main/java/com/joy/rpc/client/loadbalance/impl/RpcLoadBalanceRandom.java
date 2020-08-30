package com.joy.rpc.client.loadbalance.impl;

import com.joy.rpc.client.loadbalance.RpcLoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ai Lun on 2020-08-30.
 */
public class RpcLoadBalanceRandom implements RpcLoadBalance {

    @Override
    public String choose(List<String> addresses) {
        int index = ThreadLocalRandom.current().nextInt(addresses.size());
        return addresses.get(index);
    }

    public static String route(List<String> addresses) {
        int index = ThreadLocalRandom.current().nextInt(addresses.size());
        return addresses.get(index);
    }
}
