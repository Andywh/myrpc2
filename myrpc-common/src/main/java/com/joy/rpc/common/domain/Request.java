package com.joy.rpc.common.domain;

import java.io.Serializable;

/**
 * Created by Ai Lun on 2020-08-20.
 */


public class Request implements Serializable {
    private static final long serialVersionUID = -2524587347775862771L;

    private int requestId;

    private String clazzName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

}
