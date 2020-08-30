package com.joy.rpc.common.constant;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public interface Constant {

    int ZK_SESSION_TIMEOUT = 5000;

    int ZK_CONNECTION_TIMEOUT = 1000;

    String ZK_REGISTRY_PATH = "/registry111";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

    //String ZK_NAMESPACE = "netty-rpc";
    String ZK_NAMESPACE = "";


}
