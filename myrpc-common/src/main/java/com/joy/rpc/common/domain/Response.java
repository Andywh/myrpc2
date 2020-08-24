package com.joy.rpc.common.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Ai Lun on 2020-08-20.
 */

@Data
public class Response implements Serializable {

    private static final long serialVersionUID = 8215493329459772524L;

    private String requestId;

    private String error;

    private Object result;

    public boolean isError() {
        return error != null;
    }

}
