package com.joy.rpc.client.connect;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ai Lun on 2020-08-25.
 */
public class ChannelManager {

    // serviceName -> Channel
    // 第一步，假设每个服务只部署在一台服务器上
    // 第二步，再去实现 ConcurrentHashMap<String, List<Channel>> maps;
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    public static Channel getChannel(String serviceName) {
        return channelMap.get(serviceName);
    }

    public static void putChannel(String serviceName, Channel channel) {
        channelMap.put(serviceName, channel);
    }

}
