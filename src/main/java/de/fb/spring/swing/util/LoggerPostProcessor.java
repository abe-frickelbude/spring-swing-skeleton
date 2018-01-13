package de.fb.spring.swing.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import de.fb.spring.swing.annotations.Log;

/**
 * Custom bean post-processor that injects Slf4J loggers into beans via @Log annotation.
 * 
 * Because we want the log references to be established as early as possible, this bean should be configured with a
 * priority order of 0. This will ensure that the log injection happens before any other bean initialization (autowiring
 * etc.).
 * 
 * WARNING: Only private static fields of a bean will be initialized, in adherence to the
 * "one private static log per class" convention!
 * 
 * Please be aware that this class uses some advanced reflection functionality to write to private static fields from an
 * instance method. On one hand, this may be considered a bad programming practice.
 * 
 * On the other hand, loggers should usually be private AND static, since one usually wants only one log instance for
 * all objects of a particular class - doing it otherwise IS a bad practice.
 * 
 * @author ibragim
 * 
 */
public class LoggerPostProcessor implements BeanPostProcessor, PriorityOrdered {

    private static final Logger log = LoggerFactory.getLogger(LoggerPostProcessor.class);

    private int order;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {

        for (Field field : bean.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(Log.class) && Modifier.isStatic(field.getModifiers())) {

                try {
                    if (FieldUtils.readStaticField(field, true) == null) {
                        Logger loggerInstance = LoggerFactory.getLogger(bean.getClass());
                        FieldUtils.writeStaticField(field, loggerInstance, true);
                    }
                } catch (IllegalAccessException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        return bean;
    }
}
