package de.dyncloud.dyncloud.api;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import de.dyncloud.dyncloud.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
/**
 * Created by Alexander on 15.04.2017.
 */
public class CloudAPI {



     public static void addFreeServer(String name, String group)
        {
            File file = new File("/home/CloudSystem/", group + ".yml");
            if (!file.exists()) {
                try
                {
                    file.createNewFile();
                    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                    List<String> servers = cfg.getStringList("FreeServers");
                    if (!servers.contains(name))
                    {
                        servers.add(name);
                        cfg.set("FreeServers", servers);
                        cfg.save(file);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public static void removeFreeServer(String name, String group)
        {
            File file = new File("/home/CloudSystem/", group + ".yml");
            try
            {
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                List<String> servers = cfg.getStringList("FreeServers");
                    servers.remove(name);
                    cfg.set("FreeServers", servers);
                    cfg.save(file);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public static List<String> getFreeServers(String group)
        {
            File file = new File("/home/CloudSystem/", group + ".yml");
            if (file.exists())
            {
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                List<String> servers = cfg.getStringList("FreeServers");
                return servers;
            }
            System.out.println("Achtung ! Free Server datei null");
            return null;
        }

        public static void createServer(String name, String group, int port)
        {
            File file = new File("/home/CloudSystem/", name + ".yml");
            if (!file.exists()) {
                try
                {
                    file.createNewFile();
                    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                    cfg.set("Name", name);
                    cfg.set("Port", Integer.valueOf(port));
                    cfg.set("Group", group);
                    cfg.save(file);
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

        public static void callServer(final String group)
        {
            final File file = new File("/home/CloudSystem/", "NeedServers.yml");
            if (file.exists())
            {
                final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                List<String> need = cfg.getStringList("NeedServers");
                if (!need.contains(group))
                {
                    need.add(group);
                    cfg.set("NeedServers", need);
                    try
                    {
                        cfg.save(file);
                        Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
                            @Override
                            public void run() {
                                need.remove(group);
                                cfg.set("NeedServers",group);
                            }
                        },5L);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Integer getAllServers(String group)
        {
            File file = new File("/home/CloudSystem/groups/", group + ".yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            List<String> list = cfg.getStringList("AllServers");
            String string = (String)Collections.max(list);
            return Integer.valueOf(string);
        }
    }


