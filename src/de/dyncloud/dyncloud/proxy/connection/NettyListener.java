package de.dyncloud.dyncloud.proxy.connection;

import de.dyncloud.dyncloud.DynCloud;
import de.dyncloud.dyncloud.proxy.ProxyWrapper;
import de.dyncloud.dyncloud.proxy.byteencoder.Encoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by Alexander on 14.04.2017.
 */
public class NettyListener implements Listener {

    @EventHandler
    public void onPostLogin(final PostLoginEvent e) {
        try {
            final ProxiedPlayer p = e.getPlayer();
            final Channel ch = (Channel) ProxyWrapper.get(ProxyWrapper.get(p, "ch"), "ch");
            ch.pipeline().addAfter("packet-encoder", "encoder", (ChannelHandler)new Encoder(p));
        }
        catch (Exception ex) {
            DynCloud.severe("Could not login player" + e.getPlayer().getName());
            ex.printStackTrace();
        }
    }
}
