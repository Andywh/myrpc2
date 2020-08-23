package com.joy.rpc.client.proxy;

import com.joy.rpc.client.domain.Future;

/**
 * Created by Ai Lun on 2020-08-23.
 */
public interface RpcService {
    Future call(String funcName) throws Exception;
}
