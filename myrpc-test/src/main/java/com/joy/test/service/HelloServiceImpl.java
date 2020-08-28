package com.joy.test.service;

/**
 * Created by Ai Lun on 2020-08-27.
 */

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
