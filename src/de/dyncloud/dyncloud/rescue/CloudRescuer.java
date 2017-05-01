package de.dyncloud.dyncloud.rescue;

import de.dyncloud.dyncloud.DynCloud;
import net.md_5.bungee.BungeeCord;

/**
 * Created by Alexander on 14.04.2017.
 */
public class CloudRescuer {

    public CloudRescuer() {

    }

    public void rescueCloud() {
        BungeeCord.getInstance().broadcast(DynCloud.prefix + " §4Sorry, but the cloudsystem stopped working. stopping server..");
        BungeeCord.getInstance().stop();

    }
}
