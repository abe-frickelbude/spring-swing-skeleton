package de.fb.jvips_playground.annotations;

import jakarta.inject.Singleton;

import java.lang.annotation.*;

/**
 * Designates a class as a "java Swing view" component.
 *
 * @author ibragim
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Singleton
public @interface SwingView {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     *
     * @return the suggested component name, if any.
     */
    String value() default "";

}
