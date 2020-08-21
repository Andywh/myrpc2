package com.joy.rpc.client.proxy;

import com.joy.rpc.client.service.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class RpcProxy {

    public <T> T create(final Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //Object resut = method.invoke(proxy, args);
                        System.out.println("456");
                        return null;
                    }
                }
        );
    }

    public static void main(String[] args) {
        RpcProxy proxy = new RpcProxy();
        UserService userService = (UserService) proxy.create(UserService.class);
        userService.getName("123");
        
    }


}
