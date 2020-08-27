package com.joy.rpc.server;

import com.joy.rpc.common.annotation.RpcService;
import com.joy.rpc.server.core.NettyServer;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ai Lun on 2020-08-23.
 */

public class RpcServer extends NettyServer implements ApplicationContextAware, InitializingBean, DisposableBean {

    private Map<String, Object> serviceMap = new HashMap<>();

    public RpcServer(String serverAddress, String registryAddress) {
        super(serverAddress, registryAddress);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String interfaceName = rpcService.value().getName();
                String version = rpcService.version();
                // todo 注册 注册这一步是放外层，还是放里面呢？
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

}
