package svenhjol.meson.iface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    String description() default "";
    boolean alwaysEnabled() default false;
    boolean enabledByDefault() default true;
    boolean hasSubscriptions() default false;
}
