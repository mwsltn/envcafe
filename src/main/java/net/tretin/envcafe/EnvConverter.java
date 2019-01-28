package net.tretin.envcafe;

public interface EnvConverter<T> {
    T convert(String s) throws EnvCafeException;
}
