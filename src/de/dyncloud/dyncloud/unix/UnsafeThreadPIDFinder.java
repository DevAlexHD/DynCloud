package de.dyncloud.dyncloud.unix;

import java.lang.reflect.Field;

/**
 * Created by Alexander on 14.04.2017.
 */
public class UnsafeThreadPIDFinder {

    public UnsafeThreadPIDFinder() {

    }
    public static synchronized long getPidOfProcess(Process p) {
        long pid = -1;

        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            pid = -1;
        }
        return pid;
    }
}
