package de.dyncloud.dyncloud.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Alexander on 14.04.2017.
 */
public class ProxyWrapper {

    public static Field getField(final Class<?> clazz, final String fname) throws Exception {
        Field f = null;
        try {
            f = clazz.getDeclaredField(fname);
        }
        catch (Exception e) {
            f = clazz.getField(fname);
        }
        setAccessible(f);
        return f;
    }

    public static void setAccessible(final Field f) throws Exception {
        f.setAccessible(true);
        final Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(f, f.getModifiers() & 0xFFFFFFEF);
    }

    public static Method getMethod(final Class<?> clazz, final String mname) throws Exception {
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(mname, (Class<?>[])new Class[0]);
        }
        catch (Exception e) {
            try {
                m = clazz.getMethod(mname, (Class<?>[])new Class[0]);
            }
            catch (Exception ex) {
                return m;
            }
        }
        m.setAccessible(true);
        return m;
    }

    public static Method getMethod(final Class<?> clazz, final String mname, final Class<?>... args) throws Exception {
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(mname, args);
        }
        catch (Exception e) {
            try {
                m = clazz.getMethod(mname, args);
            }
            catch (Exception ex) {
                return m;
            }
        }
        m.setAccessible(true);
        return m;
    }

    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... args) throws Exception {
        final Constructor<?> c = clazz.getConstructor(args);
        c.setAccessible(true);
        return c;
    }

    public static Enum<?> getEnum(final Class<?> clazz, final String enumname, final String constant) throws Exception {
        final Class<?> c = Class.forName(String.valueOf(clazz.getName()) + "$" + enumname);
        final Enum[] econstants = (Enum[])c.getEnumConstants();
        Enum[] arrayOfEnum1;
        for (int j = (arrayOfEnum1 = econstants).length, i = 0; i < j; ++i) {
            final Enum<?> e = (Enum<?>)arrayOfEnum1[i];
            if (e.name().equalsIgnoreCase(constant)) {
                return e;
            }
        }
        throw new Exception("Enum constant not found " + constant);
    }

    public static Enum<?> getEnum(final Class<?> clazz, final String constant) throws Exception {
        final Class<?> c = Class.forName(clazz.getName());
        final Enum[] econstants = (Enum[])c.getEnumConstants();
        Enum[] arrayOfEnum1;
        for (int j = (arrayOfEnum1 = econstants).length, i = 0; i < j; ++i) {
            final Enum<?> e = (Enum<?>)arrayOfEnum1[i];
            if (e.name().equalsIgnoreCase(constant)) {
                return e;
            }
        }
        throw new Exception("Enum constant not found " + constant);
    }

    public static Object get(final Class<?> c, final String fname) throws Exception {
        return getField(c, fname).get(null);
    }

    public static Object get(final Object obj, final String fname) throws Exception {
        return getField(obj.getClass(), fname).get(obj);
    }

    public static Object get(final Class<?> c, final Object obj, final String fname) throws Exception {
        return getField(c, fname).get(obj);
    }

    public static void set(final Class<?> c, final String fname, final Object value) throws Exception {
        getField(c, fname).set(null, value);
    }

    public static void set(final Object obj, final String fname, final Object value) throws Exception {
        getField(obj.getClass(), fname).set(obj, value);
    }

    public static void set(final Class<?> c, final Object obj, final String fname, final Object value) throws Exception {
        getField(c, fname).set(obj, value);
    }

    public static Object invoke(final Class<?> clazz, final Object obj, final String method, final Class<?>[] args, final Object... initargs) throws Exception {
        return getMethod(clazz, method, args).invoke(obj, initargs);
    }

    public static Object invoke(final Class<?> clazz, final Object obj, final String method) throws Exception {
        return getMethod(clazz, method).invoke(obj, new Object[0]);
    }

    public static Object invoke(final Class<?> clazz, final Object obj, final String method, final Object... initargs) throws Exception {
        return getMethod(clazz, method).invoke(obj, initargs);
    }

    public static Object invoke(final Object obj, final String method) throws Exception {
        return getMethod(obj.getClass(), method).invoke(obj, new Object[0]);
    }

    public static Object invoke(final Object obj, final String method, final Object[] initargs) throws Exception {
        return getMethod(obj.getClass(), method).invoke(obj, initargs);
    }

    public static Object construct(final Class<?> clazz, final Class<?>[] args, final Object... initargs) throws Exception {
        return getConstructor(clazz, args).newInstance(initargs);
    }
}

