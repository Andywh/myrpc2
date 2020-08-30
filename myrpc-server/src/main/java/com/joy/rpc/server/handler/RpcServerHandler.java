package com.joy.rpc.server.handler;

import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.domain.Response;
import com.joy.rpc.common.util.ServiceUtil;
import com.joy.rpc.server.core.NettyServer;
import com.joy.rpc.server.registry.impl.ZooKeeperRegistryServiceImpl;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andy
 * @date 2020/08/27
 **/
public class RpcServerHandler extends SimpleChannelInboundHandler<Request> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        //System.out.println("server receive message");
        ////super.channelRead(ctx, msg);
        //System.out.println("Server received: " + request.toString());
        //Thread.sleep(3000);
        //Response response = new Response();
        //response.setError(null);
        //response.setRequestId(request.getRequestId());
        //response.setResult("this is return message");
        //ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

        // 创建并初始化 RPC 响应对象
        Response response = new Response();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Exception e) {
            logger.error("handler result failure", e);
            response.setError(e.getMessage());
        }
        // 写入 PRC 响应对象并自动关闭连接
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);


    }

    private Object handle(Request request) throws Exception {
        String clazzName = request.getClazzName();
        String version = request.getVersion();
        String serviceKey = ServiceUtil.buildServiceKey(clazzName, version);
        Object serviceBean = NettyServer.serviceMap.get(serviceKey);
        if (serviceBean == null) {
            logger.error("Can not find service implement with interface name: {} and version: {}", clazzName, version);
            return null;
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        logger.debug(serviceClass.getName());
        logger.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            logger.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < parameters.length; ++i) {
            logger.debug(parameters[i].toString());
        }

        // JDK reflect
//        Method method = serviceClass.getMethod(methodName, parameterTypes);
//        method.setAccessible(true);
//        return method.invoke(serviceBean, parameters);

        // Cglib reflect
        FastClass serviceFastClass = FastClass.create(serviceClass);
//        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
//        return serviceFastMethod.invoke(serviceBean, parameters);
        // for higher-performance
        int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
        return serviceFastClass.invoke(methodIndex, serviceBean, parameters);
    }
}
