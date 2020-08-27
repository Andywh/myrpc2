package com.joy.rpc.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC Service annotation
 * @author Andy
 * @date 2020/08/27
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    /**
     * 服务接口类
     */
    Class<?> value();

    /**
     * 服务版本号
     */
    String version() default "";
}
