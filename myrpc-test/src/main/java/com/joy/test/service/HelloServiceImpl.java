package com.joy.test.service;

import com.joy.rpc.common.annotation.RpcService;
import org.springframework.stereotype.Component;

/**
 * Created by Ai Lun on 2020-08-27.
 */
@RpcService(value = HelloService.class, version="1.0")
@Component
public class HelloServiceImpl implements HelloService{

    public HelloServiceImpl() {

    }

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello " + person.getFirstName() + " " + person.getLastName();
    }

}
