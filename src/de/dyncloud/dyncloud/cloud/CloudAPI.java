package de.dyncloud.dyncloud.cloud;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Alexander on 13.04.2017.
 */
public class CloudAPI {

    public static void addFreeServer(String name, String group)
    {
        File file = new File("/home/CloudSystem/", group + ".yml");
        if (!file.exists()) {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            List<String> servers = cfg.getStringList("FreeServers");
            if (!servers.contains(name))
            {
                servers.add(name);
                cfg.set("FreeServers", servers);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void removeFreeServer(String name, String group)
    {
        File file = new File("/home/CloudSystem/", group + ".yml");
        if (file.exists()) {
            try
            {
                Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                List<String> servers = cfg.getStringList("FreeServers");
                if (servers.contains(name))
                {
                    servers.remove(name);
                    cfg.set("FreeServers", servers);
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void createServer(String name, String group, int port)
    {
        File file = new File("/home/CloudSystem/", name + ".yml");
        if (!file.exists()) {
            try
            {
                file.createNewFile();
                Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                cfg.set("Name", name);
                cfg.set("Port", Integer.valueOf(port));
                cfg.set("Group", group);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void removeServer(String name)
    {
        File file = new File("/home/CloudSystem/", name + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }

    public static void removeServerWithID(String id, String group)
    {
        File file = new File("/home/CloudSystem/groups/", group + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try
        {
            Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            List<String> list = cfg.getStringList("AllServers");
            if (list.contains(id))
            {
                list.remove(id);
                cfg.set("AllServers", list);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
            }
        }
        catch (IOException localIOException) {}
    }

    public static void addServerWithID(String group, String id)
    {
        File file = new File("/home/CloudSystem/groups/", group + ".yml");
        try
        {
            Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            List<String> list = cfg.getStringList("AllServers");
            if (!list.contains(id)) {
                list.add(id);
            }
        }
        catch (IOException localIOException) {}
    }

}
