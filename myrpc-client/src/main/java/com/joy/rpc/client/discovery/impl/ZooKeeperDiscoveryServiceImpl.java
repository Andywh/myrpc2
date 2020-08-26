package com.joy.rpc.client.discovery.impl;

import com.joy.rpc.client.discovery.DiscoveryService;
import com.joy.rpc.common.constant.Constant;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public class ZooKeeperDiscoveryServiceImpl implements DiscoveryService {

    private final ZkClient zkClient;

    public ZooKeeperDiscoveryServiceImpl(String zkAddress) {
        this.zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
    }

    @Override
    public String discover(String serviceName) {

        try {
            // 获取 service 节点
            String servicePath = Constant.ZK_REGISTRY_PATH + "/" + serviceName;
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            //if (CollectionUtil.isEmpty(addressList)) {
            //    throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            //}
            // 获取 address 节点
            String address;
            int size = addressList.size();
            if (size == 1) {
                // 若只有一个地址，则获取该地址
                address = addressList.get(0);
                //LOGGER.debug("get only address node: {}", address);
            } else {
                // 若存在多个地址，则随机获取一个地址
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                //LOGGER.debug("get random address node: {}", address);
            }
            // 获取 address 节点的值
            String addressPath = servicePath + "/" + address;
            return zkClient.readData(addressPath);
        } finally {
            zkClient.close();
        }
    }

    public static void main(String[] args) {
        ZooKeeperDiscoveryServiceImpl zk = new ZooKeeperDiscoveryServiceImpl("127.0.0.1:2181");
        String getUser = zk.discover("getUser2");
        System.out.println(getUser);
    }

}
