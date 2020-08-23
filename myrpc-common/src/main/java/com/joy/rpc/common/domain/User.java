package com.joy.rpc.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Ai Lun on 2020-08-23.
 */
@Getter
@Setter
@ToString
public class User {

    private String name;

    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
