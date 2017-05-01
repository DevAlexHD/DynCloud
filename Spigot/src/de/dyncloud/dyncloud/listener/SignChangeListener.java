package de.dyncloud.dyncloud.listener;

import de.dyncloud.dyncloud.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Alexander on 15.04.2017.
 */
public class SignChangeListener implements Listener {

    public Main plugin;

    public SignChangeListener(Main instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onChange(SignChangeEvent e)
            throws UnknownHostException, ClassNotFoundException, IOException, InterruptedException
    {
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();
        String eins = e.getLine(0);
        String zwei = e.getLine(1);
        String drei = e.getLine(2);
        if ((eins.equalsIgnoreCase("[cloud]")) &&
                (zwei != null) && (drei != null))
        {
            File file = new File("plugins//CloudSystemSpigot//signs//", zwei);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            cfg.set("X", Double.valueOf(loc.getX()));
            cfg.set("Y", Double.valueOf(loc.getY()));
            cfg.set("Z", Double.valueOf(loc.getZ()));
            cfg.set("World", loc.getWorld().getName());
            cfg.set("ID", zwei);
            cfg.set("Name", "Offline");
            cfg.set("Group", drei);
            cfg.set("Used", Boolean.valueOf(false));
            cfg.set("Port", Integer.valueOf(0));
            cfg.set("Delete", Boolean.valueOf(true));
            try
            {
                p.sendMessage("§c[Sign] §7Schild erstellt");
                cfg.save(file);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                p.sendMessage("§c[Sign] §7Schild NICHT erstellt");
            }
        }
    }
}
