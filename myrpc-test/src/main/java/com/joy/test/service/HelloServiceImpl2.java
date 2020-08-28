package com.joy.test.service;

import com.joy.rpc.common.annotation.RpcService;
import org.springframework.stereotype.Component;

/**
 * Created by Ai Lun on 2020-08-27.
 */
@RpcService(value = HelloService.class, version="2.0")
@Component
public class HelloServiceImpl2 implements HelloService{

    public HelloServiceImpl2() {

    }

    @Override
    public String hello(String name) {
        return "Hi " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hi " + person.getFirstName() + " " + person.getLastName();
    }
}
