package com.joy.rpc.client.discovery.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.joy.rpc.client.connect.ConnectionManager;
import com.joy.rpc.client.discovery.DiscoveryService;
import com.joy.rpc.common.constant.Constant;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public class ZooKeeperDiscoveryServiceImpl implements DiscoveryService {

    public static Map<String, List<String>> serviceMap = Maps.newHashMap();

    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperDiscoveryServiceImpl.class);

    private final ZkClient zkClient;

    public ZooKeeperDiscoveryServiceImpl(String zkAddress) {
        this.zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        discoveryService();
    }

    @Override
    public void discoveryService() {
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
            UpdateConnectedServer(serviceMap);
            logger.info("serviceMap: {}", serviceMap);
        } catch (Exception e) {
            logger.info("error " + e.toString());
        }

    }

    private void UpdateConnectedServer(Map<String, List<String>> serviceMap) {
        ConnectionManager.getInstance().connectServer(serviceMap);
    }

}
