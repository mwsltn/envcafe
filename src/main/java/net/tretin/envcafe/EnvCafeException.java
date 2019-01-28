package net.tretin.envcafe;

public class EnvCafeException extends Throwable {
    public EnvCafeException(String format) {
        super(format);
    }

    public EnvCafeException(String format, Exception e) {
        super(format, e);
    }
}
