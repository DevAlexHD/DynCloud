package de.dyncloud.dyncloud.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexander on 13.04.2017.
 */
public class TimeUtil {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss", Locale.GERMAN);

    public static String formatTime() {
        return simpleDateFormat.format(new Date());
    }
}
