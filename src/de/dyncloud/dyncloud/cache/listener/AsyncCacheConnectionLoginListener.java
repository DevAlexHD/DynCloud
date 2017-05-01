package de.dyncloud.dyncloud.cache.listener;

import de.dyncloud.dyncloud.DynCloud;
import de.dyncloud.dyncloud.cache.UUIDCache;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by Alexander on 13.04.2017.
 */
public class AsyncCacheConnectionLoginListener implements Listener {

    @EventHandler
    public void onAsyncCacheConnectionCalled(PostLoginEvent ase) {
        UUIDCache uuidCache = new UUIDCache(ase.getPlayer().getUniqueId());
        uuidCache.cache();
        DynCloud.getInstance().severe("Cached uuid " + ase.getPlayer().getUniqueId().toString());
    }
}
