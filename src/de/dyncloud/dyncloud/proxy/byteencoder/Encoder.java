package de.dyncloud.dyncloud.proxy.byteencoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.Respawn;

import java.util.List;

/**
 * Created by Alexander on 14.04.2017.
 */
public class Encoder extends MessageToMessageEncoder<DefinedPacket> {

    private ProxiedPlayer p;

    public Encoder(final ProxiedPlayer p) {
        this.p = p;
    }

    protected void encode(final ChannelHandlerContext chc, final DefinedPacket packet, final List<Object> list) throws Exception {
        if (packet instanceof Respawn) {
            final Respawn respawn = (Respawn)packet;
            final UserConnection con = (UserConnection)this.p;
            if (con.getDimension() != respawn.getDimension()) {
                return;
            }
        }
        list.add(packet);
    }
}
