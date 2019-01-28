package net.tretin.envcafe;

import java.util.Arrays;

public class Example {
    /*
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
        */

    @FromEnvironment(var = "TEST_BOOL_PRIM", or = "FALSE")
    private boolean testBoolPrim;
    @FromEnvironment(var = "TEST_BOOL_OBJ", or = "FALSE")
    private Boolean testBoolObj;

    @FromEnvironment(var = "ADDRESS", or = "localhost")
    private String address;

    @FromEnvironment(var = "PORT", or = "8080")
    private int port;

    @FromEnvironment(var = "PATH", or = "/usr/bin:/usr/sbin")
    private String[] path;

    public static void main(String[] args) throws EnvCafeException {
        Example ex = EnvCafe.env(Example.class, EnvCafe.DEFAULT);
        System.out.println(ex.address);
        System.out.println(ex.port);
        Arrays.asList(ex.path).forEach(System.out::println);
    }
}
