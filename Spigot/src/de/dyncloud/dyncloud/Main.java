package de.dyncloud.dyncloud;

import de.dyncloud.dyncloud.listener.SignChangeListener;
import de.dyncloud.dyncloud.listener.SignInteractListener;
import de.dyncloud.dyncloud.sign.SignManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by Alexander on 15.04.2017.
 */
public class Main extends JavaPlugin {

    //main class

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        registerEvents();
        SignManager.updateSigns();
    }

    @Override
    public void onDisable() {
        plugin = null;
        File file = new File("plugins//CloudSystemSpigot//signs//");
        File[] files = file.listFiles();
        File[] arrayOfFile1;
        int j = (arrayOfFile1 = files).length;
        for (int i = 0; i < j; i++)
        {
            File f = arrayOfFile1[i];
            FileConfiguration cfg = getConfiguration(f);

            cfg.set("Port","");
            cfg.set("Used",false);
            cfg.set("Name","");

        }

    }

    private void registerEvents()
    {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new SignChangeListener(this), this);
        pm.registerEvents(new SignInteractListener(), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public static FileConfiguration getConfiguration(File file)
    {
        return YamlConfiguration.loadConfiguration(file);
    }
}
