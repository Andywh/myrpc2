package com.joy.rpc.server;

import com.joy.rpc.common.annotation.RpcService;
import com.joy.rpc.server.core.NettyServer;
import com.joy.rpc.server.registry.RegistryService;
import com.joy.rpc.server.registry.impl.ZooKeeperRegistryServiceImpl;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ai Lun on 2020-08-23.
 */
@Component
public class RpcServer extends NettyServer implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);


    public RpcServer(String serverAddress, String registryAddress) {
        super(serverAddress, registryAddress);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        logger.info("setApplication begin");
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String interfaceName = rpcService.value().getName();
                String version = rpcService.version();
                // todo 注册 注册这一步是放外层，还是放里面呢？最后决定还是放里层吧
                super.addService(interfaceName, version, serviceBean);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }

    @Override
    public void destroy() throws Exception {
        super.stop();
    }

}
