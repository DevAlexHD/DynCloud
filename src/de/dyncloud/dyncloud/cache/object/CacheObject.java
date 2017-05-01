package de.dyncloud.dyncloud.cache.object;

/**
 * Created by Alexander on 13.04.2017.
 */
public class CacheObject extends Object {


    public Object cacheObject;
    public CacheObject(Object object) {
        this.cacheObject = object;
    }

    public void setCacheObject(Object cacheObject) {
        this.cacheObject = cacheObject;
    }

    public Object getCacheObject() {
        return cacheObject;
    }

    public synchronized void clear() {
        setCacheObject("");
    }
}
