package de.dyncloud.dyncloud.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Alexander on 15.04.2017.
 */
public class SpigotChatHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
       if(s.equalsIgnoreCase("UNREGISTER")) {
           String[] splited = s.split("\\s+");
       }
    }
}
