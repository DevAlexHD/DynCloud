package de.dyncloud.dyncloud.sign;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import de.dyncloud.dyncloud.Main;
import de.dyncloud.dyncloud.api.CloudAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Created by Alexander on 15.04.2017.
 */
public class SignManager {

    public static ArrayList<String> getAllFiles(File directory)
    {
        if (directory.isDirectory())
        {
            File[] files = new File("plugins//CloudSystemSpigot//signs").listFiles();
            File[] arrayOfFile1;
            int j = (arrayOfFile1 = files).length;
            for (int i = 0; i < j; i++)
            {
                File file = arrayOfFile1[i];
                if (file.isFile())
                {
                    ArrayList<String> fileList = new ArrayList();
                    fileList.add(file.getName());
                    return fileList;
                }
            }
        }
        return null;
    }

    public static FileConfiguration getConfiguration(File file)
    {
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void updateSigns()
    {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.plugin, new Runnable()
        {
            public void run()
            {
                File file = new File("plugins//CloudSystemSpigot//signs//");
                File[] files = file.listFiles();
                File[] arrayOfFile1;
                int j = (arrayOfFile1 = files).length;
                for (int i = 0; i < j; i++)
                {
                    File f = arrayOfFile1[i];

                    FileConfiguration cfg = SignManager.getConfiguration(f);
                    String name = cfg.getString("Name");
                    double x = cfg.getDouble("X");
                    double y = cfg.getDouble("Y");
                    double z = cfg.getDouble("Z");
                    String world = cfg.getString("World");
                    int port = cfg.getInt("Port");
                    Location location = new Location(Bukkit.getWorld(world), x, y, z);
                    StatusSign s = new StatusSign(location, name, "127.0.0.1", port);
                    s.update();
                    SignManager.searchForUpdates();
                }
            }
        }, 250L, 5L);
    }

    public static void searchForUpdates()
    {
        File file = new File("plugins//CloudSystemSpigot//signs//");
        File[] files = file.listFiles();
        File[] arrayOfFile1;
        int j = (arrayOfFile1 = files).length;
        for (int i = 0; i < j; i++)
        {
            File f = arrayOfFile1[i];
            FileConfiguration cfg = getConfiguration(f);
            String name = cfg.getString("Name");
            double x = cfg.getDouble("X");
            double y = cfg.getDouble("Y");
            double z = cfg.getDouble("Z");
            String world = cfg.getString("World");
            int port = cfg.getInt("Port");
            boolean used = cfg.getBoolean("Used");
            String group = cfg.getString("Group");
            String id = cfg.getString("ID");
            Location location = new Location(Bukkit.getWorld("world"), x, y, z);
            for (String servers : CloudAPI.getFreeServers(group)) {
                    File server = new File("/home/CloudSystem/", servers + ".yml");
                    if (server.exists())
                    {
                        FileConfiguration serverConfig = YamlConfiguration.loadConfiguration(server);
                        cfg.set("Port", Integer.valueOf(serverConfig.getInt("Port")));
                        cfg.set("Name", serverConfig.getString("Name"));
                        cfg.set("Used", Boolean.valueOf(true));
                        cfg.set("Group", group);

                        try
                        {
                            cfg.save(f);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }

            }
            CloudAPI.removeFreeServer(name, group);
        }
    }


}
