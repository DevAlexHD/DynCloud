package de.dyncloud.dyncloud.netty;

import de.dyncloud.dyncloud.DynCloud;
import de.dyncloud.dyncloud.netty.initializer.BungeeChatInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Alexander on 15.04.2017.
 */
public class BungeeNettyServer {

    private final int port;
    private Channel channel;
    public BungeeNettyServer(int port) {

        this.port = port;

    }

    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new BungeeChatInitializer());

            try {
                bootstrap.bind(this.port).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void sendMessage(String message) {
        this.channel.writeAndFlush(message);
    }
}
