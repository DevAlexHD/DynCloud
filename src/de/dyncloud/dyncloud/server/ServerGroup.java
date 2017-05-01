
package de.dyncloud.dyncloud.server;


import de.dyncloud.dyncloud.DynCloud;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServerGroup {
    private List<TemporaryServer> temporaryServers = new ArrayList<TemporaryServer>();
    private List<TemporaryServer> startingServers = new ArrayList<TemporaryServer>();
    private String name;
    private int startupAmount;
    private int ram;

    public ServerGroup(String string, int n, int n2) {
        this.name = string;
        this.startupAmount = n;
        this.ram = n2;
    }

    public List<TemporaryServer> getTemporaryServers() {
        return this.temporaryServers;
    }

    public String getName() {
        return this.name;
    }

    public int getStartupAmount() {
        return this.startupAmount;
    }

    public void startAllServers() {
        File file = new File(DynCloud.getInstance().getFileManager().getTemplatesDirectory() + this.name);
        File file2 = new File(file, "spigot.jar");
        if (!file2.exists()) {
            DynCloud.severe("Could not start group " + this.getName() + " because spigot.jar does not exist.");
            return;
        }
        for (int i = 1; i <= this.getStartupAmount(); ++i) {
            String string = this.getName() + "-" + i;
            if (DynCloud.getInstance().getServerManager().isRunning(string)) continue;
            DynCloud.getInstance().getServerManager().startServer(this, string);
        }
    }

    public void stopAllServers() {
        for (TemporaryServer temporaryServer2 : this.getStartingServers()) {
            temporaryServer2.stop();
        }
        for (TemporaryServer temporaryServer2 : this.getTemporaryServers()) {
            temporaryServer2.stop();
        }
    }

    public void addServer(TemporaryServer temporaryServer) {
        if (!this.temporaryServers.contains(temporaryServer)) {
            this.temporaryServers.add(temporaryServer);
            return;
        }
        DynCloud.severe("Tried to add already existing server " + temporaryServer);
    }

    public void addStartingServer(TemporaryServer temporaryServer) {
        if (!this.startingServers.contains(temporaryServer)) {
            this.startingServers.add(temporaryServer);
            return;
        }
        DynCloud.severe("Tried to add already existing starting server " + temporaryServer);
    }

    public void removeServer(TemporaryServer temporaryServer) {
        if (this.temporaryServers.contains(temporaryServer)) {
            this.temporaryServers.remove(temporaryServer);
            return;
        }
        DynCloud.severe("Tried to remove not existing server " + temporaryServer);
    }

    public List<TemporaryServer> getStartingServers() {
        return this.startingServers;
    }

    public int getRam() {
        return this.ram;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        ServerGroup serverGroup = (ServerGroup) object;
        return this.name.equals(serverGroup.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return this.getName();
    }
}

