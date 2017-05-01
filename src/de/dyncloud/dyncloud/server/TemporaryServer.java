
package de.dyncloud.dyncloud.server;


import de.dyncloud.dyncloud.DynCloud;
import de.dyncloud.dyncloud.cloud.CloudAPI;
import de.dyncloud.dyncloud.unix.UnsafeThreadPIDFinder;
import de.dyncloud.dyncloud.util.TimeUtil;
import io.netty.channel.Channel;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import net.md_5.bungee.api.config.ServerInfo;
import org.apache.commons.io.FileUtils;

public class TemporaryServer {
    private ServerInfo serverInfo;
    private boolean registered = false;
    private String name;
    private ServerGroup serverGroup;
    private Channel channel;
    private long pid;
    Process process = null;
    private String state = "STARTING";

    public TemporaryServer(String string, ServerGroup serverGroup) {
        InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved("127.0.0.1", DynCloud.getInstance().getServerManager().getFreePort());
        this.serverInfo = DynCloud.getInstance().getProxy().constructServerInfo(string, inetSocketAddress, string, false);
        this.name = string;
        this.serverGroup = serverGroup;
    }

    public void start() {
        File file = new File(DynCloud.getInstance().getFileManager().getLogsDirectory(), TimeUtil.formatTime() + "_" + this.name + ".txt");
        try {
            file.createNewFile();
        }
        catch (Exception var2_2) {
            var2_2.printStackTrace();
        }
        File mod = new File(DynCloud.getInstance().getFileManager().getTemporaryDirectory() + this.name, "startserver.sh");
        try {
            Runtime.getRuntime().exec("chmod 777 " + DynCloud.getInstance().getFileManager().getTemporaryDirectory() + this.name + "./startserver.sh");
            FileUtils.copyFileToDirectory(new File(DynCloud.getFileManager().getScriptsDirectory(), "startserver.sh"),new File(DynCloud.getInstance().getFileManager().getTemporaryDirectory() + this.name + "/"));
            new File(DynCloud.getInstance().getFileManager().getTemporaryDirectory() + this.name + "/", "startserver.sh");
            File permission = new File(DynCloud.getInstance().getFileManager().getTemporaryDirectory() + this.name + "/", "startserver.sh");
            permission.setWritable(true);
            permission.setExecutable(true);
            permission.setReadable(true);


        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] params = {
                "java",
                "-Xmx" + this.getServerGroup().getRam() + "M",
                "-Dcom.mojang.eula.agree=true",
                "-jar",
                "spigot.jar",
                "-o",
                "false",
                "-p",
                String.valueOf(this.getServerInfo().getAddress().getPort()),
                "--noconsole", "" };

        ProcessBuilder b = new ProcessBuilder(params).directory(new File(DynCloud.getInstance().getFileManager().getTemporaryDirectory() + this.name));
        try {
            process = b.start();
            this.pid = UnsafeThreadPIDFinder.getPidOfProcess(process);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        DynCloud.info("Stopping server " + this.name + "...");
        unregister(false);
        process.destroy();
        DynCloud.info("Stopped " + this.name + ".");
    }

    public void register() {
        DynCloud.getInstance().getProxy().getServers().put(this.serverInfo.getName(), this.serverInfo);
        this.serverGroup.getStartingServers().remove(this);
        this.serverGroup.getTemporaryServers().add(this);
        DynCloud.getInstance().getServerManager().addServer(this.name);
        this.registered = true;
        DynCloud.info("Server " + this.name + " connected.");
        this.setState("ONLINE");
        CloudAPI.addFreeServer(this.getServerInfo().getName(),this.getServerGroup().getName());
        CloudAPI.createServer(this.getServerInfo().getName(), this.getServerGroup().getName(), this.getServerInfo().getAddress().getPort());
        CloudAPI.addServerWithID(this.getServerGroup().getName(), this.getServerInfo().getName());
    }

    public void unregister(boolean bl) {
        if (this.registered) {
            DynCloud.getInstance().getServerManager().removeServer(this.name);
            DynCloud.getInstance().getProxy().getServers().remove(this.serverInfo.getName());
            this.registered = false;
            CloudAPI.removeServerWithID(this.getServerInfo().getName(),this.getServerGroup().getName());
            CloudAPI.removeServer(this.getServerInfo().getName());
            DynCloud.info("Server " + this.name + " disconnected.");
            if (bl) {
                DynCloud.getInstance().getServerManager().startServer(this.serverGroup, this.name);
            }
            return;
        }
        DynCloud.severe("Wanted to unregister not-registered server: " + this.serverInfo.getName());
    }

    public ServerInfo getServerInfo() {
        return this.serverInfo;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRegistered() {
        return this.registered;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        TemporaryServer temporaryServer = (TemporaryServer)object;
        if (this.registered != temporaryServer.registered) {
            return false;
        }
        return this.name.equals(temporaryServer.name);
    }

    public int hashCode() {
        int n = this.registered ? 1 : 0;
        n = 31 * n + this.name.hashCode();
        return n;
    }

    public String toString() {
        return this.getName();
    }

    public ServerGroup getServerGroup() {
        return this.serverGroup;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String string) {
        this.state = string;
    }
}

