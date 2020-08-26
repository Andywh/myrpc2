package com.joy.rpc.client.discovery;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public interface DiscoveryService {
    /**
     * 根据服务名称查找服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    String discover(String serviceName);
}
