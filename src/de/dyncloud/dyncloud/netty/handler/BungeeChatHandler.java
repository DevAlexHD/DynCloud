package de.dyncloud.dyncloud.netty.handler;

import de.dyncloud.dyncloud.DynCloud;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * Created by Alexander on 15.04.2017.
 */
public class BungeeChatHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
       if(s.equalsIgnoreCase("UNREGISTER")) {
           String[] splited = s.split("\\s+");
           if(!splited[0].isEmpty()) {
               if(DynCloud.cloudShutdown) {
                   DynCloud.getServerManager().getFromServerName(splited[0]).unregister(false);
               } else {
                   DynCloud.getServerManager().getFromServerName(splited[0]).unregister(true);
               }
           }
       }
    }
}
