package com.joy.rpc.server.core;

import com.joy.rpc.common.constant.SymbolConsts;
import com.joy.rpc.server.registry.RegistryService;
import com.joy.rpc.server.registry.impl.ZooKeeperRegistryServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.spi.ServiceRegistry;

/**
 * @author Andy
 * @date 2020/08/27
 **/
public class NettyServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private Thread thread;
    private String serverAddress;
    private RegistryService registryService;

    public NettyServer(String serverAddress, String registryAddress) {
        this.serverAddress = serverAddress;
        this.registryService = new ZooKeeperRegistryServiceImpl(registryAddress);
    }

    @Override
    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(SymbolConsts.COLON);
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            ChannelFuture future = bootstrap.bind(host, port).sync();

        } catch (Exception e) {

        } finally {

        }
    }

    @Override
    public void stop() throws Exception {

    }
}
