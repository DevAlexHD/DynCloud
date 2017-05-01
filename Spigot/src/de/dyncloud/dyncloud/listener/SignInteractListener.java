package de.dyncloud.dyncloud.listener;

import de.dyncloud.dyncloud.Main;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Alexander on 15.04.2017.
 */
public class SignInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                ((e.getClickedBlock().getState() instanceof Sign)))
        {
            e.setCancelled(true);
            Sign s = (Sign)e.getClickedBlock().getState();

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try
            {
                out.writeUTF("Connect");
                out.writeUTF(s.getLine(0));
            }
            catch (IOException localIOException) {}
            p.sendPluginMessage(Main.plugin, "BungeeCord", b.toByteArray());
        }
    }
}
