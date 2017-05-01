package de.dyncloud.dyncloud.netty;

import de.dyncloud.dyncloud.netty.initializer.SpigotChatInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Alexander on 15.04.2017.
 */
public class SpigotNettyClient {

    private final String host;
    private final int port;
    private Channel channel;
    public SpigotNettyClient(String host, int port) {
        this.host = host;
        this.port = port;

    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SpigotChatInitializer());

            try {
                this.channel = bootstrap.connect(this.host,this.port).sync().channel();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendMessage(String message) {
        this.channel.writeAndFlush(message);
    }
}
