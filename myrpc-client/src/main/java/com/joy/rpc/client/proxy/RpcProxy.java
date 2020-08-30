package com.joy.rpc.client.proxy;

import com.alibaba.fastjson.JSON;
import com.joy.rpc.client.RpcClient;
import com.joy.rpc.client.connect.ConnectionManager;
import com.joy.rpc.client.domain.Future;
import com.joy.rpc.client.handler.RpcClientHandler;
import com.joy.rpc.common.domain.Request;
import com.joy.rpc.common.util.ServiceUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class RpcProxy<T> implements RpcService {

    private Class<T> clazz;
    private String version;

    public RpcProxy(Class<T> clazz, String version) {
        this.clazz = clazz;
        this.version = version;
    }

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

                        RpcClientHandler.send(request);

                        //log.info("request: {}" + JSON.toJSONString(request));
                        //RpcClientHandler handler =
                        return null;
                    }
                }
        );
    }

    public static <T> T create(final Class<?> interfaceClass, String version) {
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
                        request.setVersion(version);
                        Future future = RpcClientHandler.send(request);
                        return future;
                    }
                }
        );
    }

    public Future call(String funcName, Object... args) throws Exception {
        Request request = createRequest(this.clazz.getName(), funcName, args);
        Future future = RpcClientHandler.send(request);
        return future;
    }

    private Request createRequest(String clazzName, String methodName, Object[] args) {
        Request request = new Request();
        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);
        request.setClazzName(clazzName);
        request.setMethodName(methodName);
        request.setParameters(args);
        request.setVersion(version);

        Class[] parameterTypes = new Class[args.length];
        // Get the right class type
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        request.setParameterTypes(parameterTypes);

        return request;
    }

    private Class<?> getClassType(Object obj) {
        Class<?> classType = obj.getClass();
        String typeName = classType.getName();
        switch (typeName) {
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
        }

        return classType;
    }

}
