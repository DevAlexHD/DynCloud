package de.dyncloud.dyncloud.cache;


import de.dyncloud.dyncloud.cache.object.CacheObject;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alexander on 13.04.2017.
 */
public class UUIDCache {

    public HashMap<Object,CacheObject> cloudCache = new HashMap<>();
    public UUID uuid;
    public Object object;
    public CacheObject cObject;
    public ProxiedPlayer cachePlayer;

    public UUIDCache(Object object,CacheObject cObject) {
        this.object = object;
        this.cObject = cObject;

    }

    public UUIDCache(UUID uniqueId) {
        super();
    }


    public UUID getUUID() {
        return this.uuid;
    }

    public synchronized void cache() {
        getMapCache().put(uuid,cachePlayer);
    }

    public synchronized HashMap getMapCache() {
        return this.cloudCache;
    }

    public synchronized ConcurrentHashMap.KeySetView getCacheSet() {
        return (ConcurrentHashMap.KeySetView) getMapCache().keySet();
    }
}
