package com.joy.rpc.server.registry.impl;

import com.joy.rpc.common.constant.Constant;
import com.joy.rpc.server.registry.RegistryService;
import org.I0Itec.zkclient.ZkClient;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public class ZooKeeperRegistryServiceImpl implements RegistryService {

    private final ZkClient zkClient;

    public ZooKeeperRegistryServiceImpl(String zkAddress) {
        this.zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        // 创建 registry 节点（持久）
        String registryPath = Constant.ZK_REGISTRY_PATH;
        // 创建 service 节点（持久）
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
        }
        // 创建 address 节点（临时）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
        }
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
    }

    public static void main(String[] args) {
        ZooKeeperRegistryServiceImpl zk = new ZooKeeperRegistryServiceImpl("127.0.0.1:2181");
        zk.register("getUser", "127.0.0.1:8764");
        zk.register("getUser2", "127.0.0.1:8764");
        zk.register("getUser3", "127.0.0.1:8764");
    }
}
