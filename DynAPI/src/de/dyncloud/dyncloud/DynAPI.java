package de.dyncloud.dyncloud;

import de.dyncloud.dyncloud.netty.SpigotNettyClient;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;


import java.io.*;

/**
 * Created by Alexander on 16.04.2017.
 */
public class DynAPI extends JavaPlugin implements PluginMessageListener {

    public String serverName = "";
    public SpigotNettyClient client;
    public DynAPI plugin;
    @Override
    public void onEnable() {
        plugin = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        getServerName();
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                getServerName();
            }
        },40L);
        new Thread(new Runnable() {
            @Override
            public void run() {
                client = new SpigotNettyClient("127.0.0.1",8000);
                client.run();
            }
        }).start();

    }

    @Override
    public void onDisable() {
        this.client.sendMessage("UNREGISTER " + serverName);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        try {
            if (!channel.equals("BungeeCord")) return;
            Bukkit.broadcastMessage("Received a bungeemessage");
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
            String subchannel = in.readUTF();
            if (subchannel.equals("GetServer")) {
                Bukkit.broadcastMessage("Received a bungeemessage GetServer");
                String servername = in.readUTF();
                System.out.println(subchannel + "\n" + servername);
                this.serverName = servername;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getServerName() {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("GetServer");
            Bukkit.getServer().sendPluginMessage(this, "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
