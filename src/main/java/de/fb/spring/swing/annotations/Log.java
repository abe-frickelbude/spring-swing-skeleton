package de.fb.spring.swing.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom marker annotation for injecting slf4j loggers into Spring beans a-la SEAM @Logger injection.
 * 
 * @author ibragim
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

}
