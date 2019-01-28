package net.tretin.envcafe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FromEnvironment {
    /**
     * The environment variable to read in for this var.
     */
    String var();
    String or();
    boolean optional() default false;
}
