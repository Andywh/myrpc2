package com.joy.rpc.client.loadbalance;

import java.util.List;

/**
 * Created by Ai Lun on 2020-08-30.
 */
public interface RpcLoadBalance {

    String choose(List<String> addresses);
}
