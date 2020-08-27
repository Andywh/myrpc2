package com.joy.rpc.server.registry;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public interface RegistryService {

    /**
     * 注册服务名称与服务地址
     *
     * @param serviceName    服务名称
     * @param serviceAddress 服务地址
     */
    void register(String serviceName, String serviceAddress);

    /**
     * 取消注册
     *
     */
    void unregister();
}
