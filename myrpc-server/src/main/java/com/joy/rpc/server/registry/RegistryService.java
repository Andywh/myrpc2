package com.joy.rpc.server.registry;

import java.util.Map;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public interface RegistryService {

    /**
     * 注册服务名称与服务地址
     *
     * @param serviceAddress    服务地址
     * @param serviceMap        服务集合
     */
    void register(String serviceAddress, Map<String, Object> serviceMap);

    /**
     * 取消注册
     *
     */
    void unregister();
}
