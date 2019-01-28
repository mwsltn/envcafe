package net.tretin.envcafe;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnvCafe {

    public static final Map<Class<?>, EnvConverter<?>> DEFAULT;
    static {
        Map<Class<?>, EnvConverter<?>> m = new HashMap<>();
        m.put(boolean.class, Boolean::valueOf);
        m.put(Boolean.class, Boolean::valueOf);

        m.put(byte.class, (s) -> Byte.valueOf(s, 16));
        m.put(Byte.class, (s) -> Byte.valueOf(s, 16));
        m.put(byte[].class, (s) -> Base64.getDecoder().decode(s));

        m.put(char.class, (s) -> s.charAt(0));
        m.put(Character.class, (s) -> s.charAt(0));

        m.put(short.class, Short::valueOf);
        m.put(Short.class, Short::valueOf);

        m.put(int.class, Integer::valueOf);
        m.put(Integer.class, Integer::valueOf);

        m.put(long.class, Long::valueOf);
        m.put(Long.class, Long::valueOf);

        m.put(float.class, Float::valueOf);
        m.put(Float.class, Float::valueOf);

        m.put(double.class, Double::valueOf);
        m.put(Double.class, Double::valueOf);

        m.put(String.class, (s) -> s);
        m.put(String[].class, (s) -> s.split(":"));

        DEFAULT = Collections.unmodifiableMap(m);
    }

    public static <T> T env(Class<T> clazz, Map<Class<?>, EnvConverter<?>> converters) throws EnvCafeException {
        if (clazz == null || converters == null) {
            throw new IllegalArgumentException();
        }

        T t;

        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new EnvCafeException(String.format("can't instantiate class '%s'", clazz.getCanonicalName()), e);
        }

        for (Field f : t.getClass().getDeclaredFields()) {
            FromEnvironment a = null;
            String value = null;
            if ((a = f.getAnnotation(FromEnvironment.class)) != null) {
                if ((value = System.getenv(a.var())) != null) {
                    EnvConverter<?> c = converters.get(f.getType());
                    if (c == null) {
                        throw new EnvCafeException(
                                String.format("no converter for type: '%s'", f.getType().getCanonicalName())
                        );
                    }
                    Object o;
                    try {
                        o = c.convert(value);
                    } catch (Exception e) {
                        // This is okay here since its tightly constrained to some method that might do nasty shit.
                        throw new EnvCafeException(
                                String.format("could not convert to '%s'", f.getType().getCanonicalName()), e
                        );
                    }
                    try {
                        f.setAccessible(true);
                        f.set(t, o);
                    } catch (IllegalAccessException e) {
                        throw new EnvCafeException(
                                String.format(
                                        "can't access field: '%s'.'%s'",
                                        f.getClass().getCanonicalName(), f.getName()
                                ),
                                e
                        );
                    } finally {
                        f.setAccessible(false);
                    }
                } else {
                    throw new EnvCafeException(String.format("Environment variable '%s' was not set.", a.var()));
                }
            }
        }

        return t;
    }

}
