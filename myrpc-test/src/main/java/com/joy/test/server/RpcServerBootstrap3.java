package com.joy.test.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Andy
 * @date 2020/08/28
 **/
public class RpcServerBootstrap3 {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("server-spring3.xml");
    }
}
