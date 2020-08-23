package zhouquan;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class EchoServer {

    private final int port;

    public EchoServer(int port) { this.port = port; }

    public static void main(String[] args) throws Exception {
        // 参数检查
        int port = 8894;
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)          // 指定使用的channel类型
                    .localAddress(new InetSocketAddress(port))      // 设置地址，用于监听链接
                    // 当一个新连接到达时，一个新的子Channel会被创建，ChannelInitializer则把EchoServerHandler添加到子Channel的ChannelPipeline中
                    .childHandler(new ChannelInitializer<SocketChannel>() {  // 添加一个EchoServerHandler到子Cahnnel的ChannelPipeline中
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(serverHandler);
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();    // 异步地绑定服务器
            future.channel().closeFuture().sync();             // 获取Channel的CloseFuture并阻塞当前线程直到它完成
        } finally {
            group.shutdownGracefully().sync();                 // 关闭EventLoopGroup，释放所有资源
        }
    }
}
