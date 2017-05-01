package de.dyncloud.dyncloud.files;

import de.dyncloud.dyncloud.DynCloud;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;

/**
 * Created by Alexander on 13.04.2017.
 */
public class FileManager {
    private String pluginsDirectory = "plugins/DynCloud/";
    private String templatesDirectory = this.pluginsDirectory + "templates/";
    private String temporaryDirectory = this.pluginsDirectory + "temporary/";
    private String scriptsDirectory = this.pluginsDirectory + "scripts/";
    private String configsDirectory = this.pluginsDirectory + "configs/";
    private String logsDirectory = this.pluginsDirectory + "logs/";
    private File configFile;
    private File groupsFile;
    private Configuration config;
    private Configuration groups;

    public FileManager() {
        this.init();
    }

    private void init() {
        try {
            File file = new File(this.templatesDirectory);
            File file2 = new File(this.temporaryDirectory);
            File file3 = new File(this.scriptsDirectory);
            File file4 = new File(this.configsDirectory);
            File file5 = new File(this.logsDirectory);
            file.mkdirs();
            file2.mkdirs();
            file3.mkdirs();
            file4.mkdirs();
            file5.mkdirs();
            this.configFile = new File(this.configsDirectory, "config.yml");
            if (!this.configFile.exists()) {
                this.configFile.createNewFile();
            }



            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configFile);
            this.groupsFile = new File(this.configsDirectory, "groups.yml");
            this.groupsFile.createNewFile();
            this.groups = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.groupsFile);
            File file6 = new File(this.getScriptsDirectory(), "startserver.sh");
           Files.copy(this.getClass().getResourceAsStream("/netty/startserver.sh"), file6.toPath(), new CopyOption[0]);
            file6.setExecutable(true);
        }
        catch (Exception var1_2) {
            DynCloud.severe("Exception while initializing files:");
            var1_2.printStackTrace();
        }
    }

    public String getTemplatesDirectory() {
        return this.templatesDirectory;
    }

    public String getTemporaryDirectory() {
        return this.temporaryDirectory;
    }

    public String getScriptsDirectory() {
        return this.scriptsDirectory;
    }

    public String getLogsDirectory() {
        return this.logsDirectory;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public File getGroupsFile() {
        return this.groupsFile;
    }

    public Configuration getConfig() {
        return this.config;
    }

    public Configuration getGroups() {
        return this.groups;
    }
}

