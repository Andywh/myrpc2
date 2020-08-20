package com.joy.rpc.client.service.impl;

import com.joy.rpc.client.service.UserService;

/**
 * Created by Ai Lun on 2020-08-20.
 */
public class UserServiceImpl implements UserService {

    public String getName(String str) {
        return "Andy + 1 " + str;
    }

}
