package com.joy.rpc.common.util;

/**
 * Created by Ai Lun on 2020-08-27.
 */
public class ServiceUtil {

    public static final String SERVICE_CONCAT_TOKEN = "#";

    public static String buildServiceKey(String interfaceName, String version) {
        String serviceKey = interfaceName;
        if (version != null && version.trim().length() > 0) {
            serviceKey += SERVICE_CONCAT_TOKEN.concat(version);
        }
        return serviceKey;
    }

}
