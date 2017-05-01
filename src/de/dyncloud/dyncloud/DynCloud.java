package de.dyncloud.dyncloud;

import de.dyncloud.dyncloud.cloud.CloudAPI;
import de.dyncloud.dyncloud.motd.MOTDManager;
import de.dyncloud.dyncloud.asyncthread.MySQL;
import de.dyncloud.dyncloud.cache.listener.AsyncCacheConnectionLoginListener;
import de.dyncloud.dyncloud.cloud.CloudManager;
import de.dyncloud.dyncloud.commands.DynCloudCommand;
import de.dyncloud.dyncloud.commands.StopCloudCommand;
import de.dyncloud.dyncloud.files.FileManager;
import de.dyncloud.dyncloud.netty.BungeeNettyServer;
import de.dyncloud.dyncloud.netty.handler.BungeeChatHandler;
import de.dyncloud.dyncloud.proxy.connection.NettyListener;
import de.dyncloud.dyncloud.rescue.CloudRescuer;
import de.dyncloud.dyncloud.server.ServerGroup;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alexander on 12.04.2017.
 */
public class DynCloud extends Plugin {

    public static DynCloud instance;
    public static FileManager fileManager;
    public static CloudManager cloudManager;
    public static MOTDManager motdManager;
    public static MySQL mysql;
    public static boolean cloudShutdown;
    public static String prefix = "§c«§cDynCloud» §a ";

    @Override
    public void onEnable() {
        instance = this;
        fileManager = new FileManager();
        motdManager = new MOTDManager();
        cloudManager = new CloudManager();
        cloudShutdown = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BungeeNettyServer server = new BungeeNettyServer(8000);
                server.run();
            }
        }).start();

        registerCloudListeners();
        BungeeCord.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                for(ServerGroup gr : getServerManager().getGroups()) {
                }
                            for(ServerGroup groups : getServerManager().getGroups()) {
                                int max = groups.getStartupAmount() + 1;
                                if(groups.getTemporaryServers().size() > max || groups.getStartingServers().size() > max) {
                                    return;
                                } else {
                                    if(groups.getTemporaryServers().size() > 60 || groups.getStartingServers().size() > 60) {
                                        return;
                                    } else {
                                        for (int i = 1; i <= groups.getTemporaryServers().size(); ++i) {
                                            String string = groups.getName() + "-" + i;
                                            DynCloud.getInstance().getServerManager().startServer(groups, string);

                                        }
                                    }
                                }

                            }
            }
        },4L,4L, TimeUnit.SECONDS);

        //String name, File path, File toPath
        this.getProxy().getPluginManager().registerCommand(this, new DynCloudCommand());
        this.getProxy().getPluginManager().registerCommand(this, new StopCloudCommand("stopcloud"));
        severe(prefix + " §cStarting CloudSystem!");
        this.getServerManager().startAllServers();

    }


    @Override
    public void onDisable() {
        this.getServerManager().stopAllServers();
        severe(prefix + " §cStopped all servers. Shutting down DynCloud.. Good bye!");
        BungeeCord.getInstance().getServers().clear();
        CloudRescuer cloudRescuer = new CloudRescuer();
        cloudRescuer.rescueCloud();
    }

    public static DynCloud getInstance() {
        return instance;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public String getFileName() {
        DynCloud dynCloud = this;
        String string = dynCloud.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        String[] arrstring = string.split("/");
        String string2 = arrstring[arrstring.length - 1];
        return string2;
    }


    public static void severe(String message) {
        BungeeCord.getInstance().getConsole().sendMessage(message);
    }

    public static void info(String message) {
        BungeeCord.getInstance().getConsole().sendMessage(message);
    }

    public static CloudManager getServerManager() {
        return cloudManager;
    }

    private void registerCloudListeners() {
        BungeeCord.getInstance().getPluginManager().registerListener(this, new AsyncCacheConnectionLoginListener());
        BungeeCord.getInstance().getPluginManager().registerListener(this, new NettyListener());
    }

    public MOTDManager getMOTDManager() {
        return motdManager;
    }

    private void registerBungeeCommands() {

    }


}
