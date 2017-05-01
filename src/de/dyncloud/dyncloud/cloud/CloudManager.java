package de.dyncloud.dyncloud.cloud;

import de.dyncloud.dyncloud.DynCloud;
import de.dyncloud.dyncloud.server.ServerGroup;
import de.dyncloud.dyncloud.server.TemporaryServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 13.04.2017.
 */
public class CloudManager {
    private List<Integer> portsInUse = new ArrayList<Integer>();
    private List<ServerGroup> groups;
    private List<String> startedServers = new ArrayList<String>();

    public CloudManager() {
        this.init();
    }

    private void init() {
        this.readGroups();
    }

    public void startAllServers() {
        for (ServerGroup serverGroup : this.groups) {
            serverGroup.startAllServers();
        }
    }

    public void stopAllServers() {
        for (ServerGroup serverGroup : this.groups) {
            serverGroup.stopAllServers();
            for (TemporaryServer server : serverGroup.getTemporaryServers()) {
                CloudAPI.removeFreeServer(server.getName(), serverGroup.getName());
            }
        }
    }

    public void readGroups() {
        this.groups = new ArrayList<ServerGroup>();
        Configuration configuration = DynCloud.getInstance().getFileManager().getGroups();
        for (String string : configuration.getKeys()) {
            ServerGroup serverGroup = new ServerGroup(string, configuration.getInt(string + ".onlineAmount"), configuration.getInt(string + ".ramInGigabyte"));
            this.groups.add(serverGroup);
        }
    }

    public int getFreePort() {
        for (int i = 40000; i < 50000; ++i) {
            if (!this.isPortFree(i)) continue;
            this.registerPort(i);
            return i;
        }
        return 25565;
    }

    public boolean isPortFree(int n) {
        return !this.portsInUse.contains(n);
    }

    public void registerPort(int n) {
        if (this.isPortFree(n)) {
            this.portsInUse.add(n);
            return;
        }
        DynCloud.severe("Error: Attemping to register used port: " + n);
    }

    public void unregisterPort(int n) {
        if (!this.isPortFree(n)) {
            this.portsInUse.remove(n);
            return;
        }
        DynCloud.severe("Error: Attemping to unregister not registered port: " + n);
    }

    public ServerInfo getRandomLobbyServer() {
        for (ServerGroup serverGroup : this.getGroups()) {
            if (!serverGroup.getName().equalsIgnoreCase("lobby") || serverGroup.getTemporaryServers().size() == 0)
                continue;
            return serverGroup.getTemporaryServers().get(new Random().nextInt(serverGroup.getTemporaryServers().size())).getServerInfo();
        }
        return DynCloud.getInstance().getProxy().getServerInfo(DynCloud.getInstance().getFileManager().getConfig().getString("fallback"));
    }

    public void startServer(ServerGroup serverGroup, String string) {
        DynCloud.getInstance().getProxy().getScheduler().runAsync((Plugin) DynCloud.getInstance(), () -> {
                    this.startServerFromAsyncContext(serverGroup, string);
                }
        );
    }

    public void startServerFromAsyncContext(ServerGroup serverGroup, String string) {
        double d;
        block7:
        {
            DynCloud.getInstance();
            DynCloud.info("Starting server " + string + "...");
            d = System.currentTimeMillis();
            File file = new File(DynCloud.getInstance().getFileManager().getTemplatesDirectory() + serverGroup.getName());
            File file2 = new File(file, "spigot.jar");
            if (!file2.exists()) {
                DynCloud.severe("Could not start server " + string + " because spigot.jar does not exist.");
                return;
            }
        }
        try {
            File file = new File(DynCloud.getInstance().getFileManager().getTemporaryDirectory() + string);
            if (file.exists()) {
                FileUtils.deleteDirectory(file);
            }
            FileUtils.copyDirectory(new File(DynCloud.getInstance().getFileManager().getTemplatesDirectory() + serverGroup.getName()), file);
            TemporaryServer temporaryServer = new TemporaryServer(string, serverGroup);
            temporaryServer.start();
            temporaryServer.register();
            serverGroup.addStartingServer(temporaryServer);
            double d2 = System.currentTimeMillis();
            DynCloud.getInstance();
            DynCloud.info("Successfully started server " + string + " in " + (d2 - d) / 1000.0 + " seconds.");
        } catch (Exception var8_9) {
            DynCloud.severe("Error while starting server " + string + ":");
            var8_9.printStackTrace();
        }
    }

    public boolean isRunning(String string) {
        return this.startedServers.contains(string);
    }

    public void addServer(String string) {
        if (!this.isRunning(string)) {
            this.startedServers.add(string);
            return;
        }
        DynCloud.severe("Tried to add already running server: " + string);
    }

    public void removeServer(String string) {
        if (this.startedServers.contains(string)) {
            this.startedServers.remove(string);
            return;
        }
        DynCloud.severe("Tried to remove not started server: " + string);
    }

    public boolean groupExists(ServerGroup serverGroup) {
        return this.groups.contains(serverGroup);
    }

    public ServerGroup getGroupByName(String string) {
        for (ServerGroup serverGroup : this.groups) {
            if (!serverGroup.getName().equals(string)) continue;
            return serverGroup;
        }
        return null;
    }

    public void addGroup(ServerGroup serverGroup) {
        DynCloud.getInstance().getFileManager().getGroups().set(serverGroup.getName() + ".onlineAmount", serverGroup.getStartupAmount());
        DynCloud.getInstance().getFileManager().getGroups().set(serverGroup.getName() + ".ramInGigabyte", serverGroup.getRam());
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(DynCloud.getInstance().getFileManager().getGroups(), DynCloud.getInstance().getFileManager().getGroupsFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!this.groups.contains(serverGroup)) {
            this.groups.add(serverGroup);
        }
        serverGroup.startAllServers();
    }

    public void removeGroup(String string) {
        ServerGroup serverGroup = this.getGroupByName(string);
        serverGroup.stopAllServers();
        this.groups.remove(serverGroup);
        DynCloud.getInstance().getFileManager().getGroups().set(string, null);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(DynCloud.getInstance().getFileManager().getGroups(), DynCloud.getInstance().getFileManager().getGroupsFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ServerGroup> getGroups() {
        return this.groups;
    }

    public TemporaryServer getFromServerName(String string) {
        for (ServerGroup serverGroup : this.getGroups()) {
            for (TemporaryServer temporaryServer2 : serverGroup.getStartingServers()) {
                if (!temporaryServer2.getName().equals(string)) continue;
                return temporaryServer2;
            }
            for (TemporaryServer temporaryServer2 : serverGroup.getTemporaryServers()) {
                if (!temporaryServer2.getName().equals(string)) continue;
                return temporaryServer2;
            }
        }
        return null;
    }
}

