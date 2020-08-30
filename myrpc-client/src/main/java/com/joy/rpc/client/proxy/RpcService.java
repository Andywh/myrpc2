package com.joy.rpc.client.proxy;

import com.joy.rpc.client.domain.Future;

public interface RpcService {
    Future call(String funcName, Object... args) throws Exception;
}