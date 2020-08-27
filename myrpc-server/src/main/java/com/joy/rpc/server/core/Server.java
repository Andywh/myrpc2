package com.joy.rpc.server.core;

/**
 * 服务器的启动与关闭
 * @author Andy
 * @date 2020/08/27
 **/
public interface Server {

    /**
     * start server
     *
     * @param
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * stop server
     *
     * @param
     * @throws Exception
     */
    void stop() throws Exception;
}
