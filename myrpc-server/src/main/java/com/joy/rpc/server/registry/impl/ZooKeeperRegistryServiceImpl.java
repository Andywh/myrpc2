package com.joy.rpc.server.registry.impl;

import com.joy.rpc.common.constant.Constant;
import com.joy.rpc.server.core.NettyServer;
import com.joy.rpc.server.registry.RegistryService;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public class ZooKeeperRegistryServiceImpl implements RegistryService {

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperRegistryServiceImpl.class);

    // 注册格式:
    // /registry/serviceName -> 127.0.0.1:8876
    //

    private final ZkClient zkClient;

    public ZooKeeperRegistryServiceImpl(String zkAddress) {
        this.zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
    }

    public void register(String serviceName, String serviceAddress) {
        // 创建 registry 节点（持久）
        String registryPath = Constant.ZK_REGISTRY_PATH;
        // 创建 service 节点（持久）
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            logger.info("create registry node: {} -> {}", registryPath, serviceAddress);
        }
        // 创建 address 节点（临时）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            logger.info("create service node: {} -> {}", servicePath, serviceAddress);
        }
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        logger.info("create address node: {} -> {}", addressNode, serviceAddress);
    }

    @Override
    public void register(String serviceAddress, Map<String, Object> serviceMap) {
        for (String key : serviceMap.keySet()) {
            register(key, serviceAddress);
        }
    }

    @Override
    public void unregister() {

    }

}
