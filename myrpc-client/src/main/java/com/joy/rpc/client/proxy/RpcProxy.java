package com.joy.rpc.client.proxy;

import com.alibaba.fastjson.JSON;
import com.joy.rpc.client.service.UserService;
import com.joy.rpc.common.domain.Request;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class RpcProxy {

    public static <T> T create(final Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // todo 这里要把数据发送出去
                        Request request = new Request();
                        String requestId = UUID.randomUUID().toString();
                        request.setRequestId(requestId);
                        request.setClazzName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
                        System.out.println(JSON.toJSONString(request));
                        //RpcClient.send(request);

                        //log.info("request: {}" + JSON.toJSONString(request));
                        //RpcClientHandler handler =
                        return null;
                    }
                }
        );
    }

    public static void main(String[] args) {
        UserService userService = (UserService) RpcProxy.create(UserService.class);
        userService.getName("123");
    }


}
