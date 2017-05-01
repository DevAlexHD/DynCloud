package de.dyncloud.dyncloud.sign;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;

import de.dyncloud.dyncloud.api.CloudAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
/**
 * Created by Alexander on 15.04.2017.
 */

public class StatusSign {
    private Location location;
    private static Sign sign;
    private static String name;
    private String ip;
    private int port;
    private static int time;

    public StatusSign(final Location location, final String name, final String ip, final int port) {
        this.location = location;
        StatusSign.sign = (Sign)location.getBlock().getState();
        StatusSign.name = name;
        this.ip = ip;
        this.port = port;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return StatusSign.name;
    }

    public String getIP() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public void update() {
        try {
            final Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.ip, this.port), 1000);
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            final DataInputStream in = new DataInputStream(socket.getInputStream());
            out.write(254);
            final StringBuilder str = new StringBuilder();
            int b;
            while ((b = in.read()) != -1) {
                if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                    str.append((char) b);
                }
            }
            getSignHolder(this.location).getBlock().setTypeIdAndData(159, (byte) 5, true);
            final String[] data = str.toString().split("§");
            final String motd = data[0];
            final int onlinePlayers = Integer.valueOf(data[1]);
            final int maxPlayers = Integer.valueOf(data[2]);
            if (onlinePlayers == maxPlayers || onlinePlayers > maxPlayers) {
                StatusSign.sign.setLine(1, "§8«§6Lobby§8»");
                StatusSign.sign.setLine(2, "§c" + onlinePlayers + "§8 / §c" + maxPlayers);
                final File file = new File("plugins//CloudSystemSpigot//signs//");
                final File[] files = file.listFiles();
                File[] array;
                for (int length = (array = files).length, i = 0; i < length; ++i) {
                    final File f = array[i];
                    final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(f);
                    final String name = cfg.getString("Name");
                    final double x = cfg.getDouble("X");
                    final double y = cfg.getDouble("Y");
                    final double z = cfg.getDouble("Z");
                    final String world = cfg.getString("World");
                    final int port = cfg.getInt("Port");
                    final boolean used = cfg.getBoolean("Used");
                    final String group = cfg.getString("Group");
                    final String id = cfg.getString("ID");
                    final Location location = new Location(Bukkit.getWorld("world"), x, y, z);
                    if (name.equalsIgnoreCase(this.getName())) {
                        cfg.set("Used", false);
                        cfg.save(f);
                    }
                }
            } else {
                StatusSign.sign.setLine(0, name);
                StatusSign.sign.setLine(1, "§8«§aLobby§8»");
                StatusSign.sign.setLine(2, "§7" + onlinePlayers + "§8 / §6" + maxPlayers);
                StatusSign.sign.setLine(0, StatusSign.name);
            }
            socket.close();
        } catch (Exception e) {
            StatusSign.sign.setLine(0, "Warte");
            StatusSign.sign.setLine(1, "auf");
            StatusSign.sign.setLine(2, "Server");
        /*
            final File file2 = new File("plugins//CloudSystemSpigot//signs//");
             */

        }
        StatusSign.sign.update();
    }


    public static Location getSignHolder(final Location signLocation) {
        if (!signLocation.getBlock().getType().equals(Material.WALL_SIGN)) {
            return null;
        }
        switch (signLocation.getBlock().getData()) {
            case 2: {
                return signLocation.clone().add(0.0, 0.0, 1.0);
            }
            case 3: {
                return signLocation.clone().add(0.0, 0.0, -1.0);
            }
            case 4: {
                return signLocation.clone().add(1.0, 0.0, 0.0);
            }
            case 5: {
                return signLocation.clone().add(-1.0, 0.0, 0.0);
            }
            default: {
                return null;
            }
        }
    }
}
