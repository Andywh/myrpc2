package com.joy.rpc.client.domain;

/**
 * Created by Ai Lun on 2020-08-21.
 */
public interface AsyCallback {

    void success(Object result);

    void fail(Exception e);

}
