package com.joy.rpc.client.discovery.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.joy.rpc.client.discovery.DiscoveryService;
import com.joy.rpc.common.constant.Constant;
import com.joy.rpc.common.zookeeper.CuratorClient;
import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public class ZooKeeperDiscoveryServiceImpl implements DiscoveryService {

    private Map<String, List<String>> serviceMap = Maps.newHashMap();

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperDiscoveryServiceImpl.class);

    private final ZkClient zkClient;

    public ZooKeeperDiscoveryServiceImpl(String zkAddress) {
        this.zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        discoveryService();
    }

    private void discoveryService() {
        try {
            List<String> nodeList = zkClient.getChildren(Constant.ZK_REGISTRY_PATH);
            for (String seviceKey : nodeList) {
                List<String> servicePathList = zkClient.getChildren(Constant.ZK_REGISTRY_PATH + "/" + seviceKey);
                //logger.info("servicePathList: {}", servicePathList);
                for (String servicePath : servicePathList) {
                    String address = zkClient.readData(Constant.ZK_REGISTRY_PATH + "/" + seviceKey + "/" + servicePath);
                    //logger.info("address: {}", address);
                    if (serviceMap.containsKey(seviceKey)) {
                        serviceMap.get(seviceKey).add(address);
                    } else {
                        serviceMap.put(seviceKey, Lists.newArrayList(address));
                    }
                }
            }
            logger.info("serviceMap: {}", serviceMap);
        } catch (Exception e) {
            logger.info("error " + e.toString());
        }


    }

    public static void main(String[] args) {
        ZooKeeperDiscoveryServiceImpl zk = new ZooKeeperDiscoveryServiceImpl("127.0.0.1:2181");
        String getUser = zk.discover("getUser2");
        System.out.println(getUser);
    }

    @Override
    public String discover(String serviceName) {
        return null;
    }

}
