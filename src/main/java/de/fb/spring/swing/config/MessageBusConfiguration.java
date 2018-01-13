package de.fb.spring.swing.config;

import java.util.ArrayList;
import java.util.List;
import org.rribbit.RequestResponseBus;
import org.rribbit.creation.SpringBeanClassBasedListenerObjectCreator;
import org.rribbit.util.RRiBbitUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RriBbit system-wide message bus configuration.
 * 
 * @author Ibragim Kuliev
 *
 */
@Configuration
public class MessageBusConfiguration {

    /**
     * This a spring bean scanner that automatically creates any listener objects declared
     * in the classes of the specified packages.
     * 
     * @return
     */
    @Bean
    public SpringBeanClassBasedListenerObjectCreator listenerFactory() {

        SpringBeanClassBasedListenerObjectCreator factory = new SpringBeanClassBasedListenerObjectCreator();

        List<String> packageNames = new ArrayList<>();
        packageNames.add("de.fb.spring.swing.view");
        packageNames.add("de.fb.spring.swing.controller");

        factory.setPackageNames(packageNames);
        factory.setScanSubpackages(true);
        return factory;
    }

    /**
     * Local (i.e. not network-enabled) request-response bus singleton.
     * 
     * @return
     */
    @Bean
    public RequestResponseBus messageBus() {
        return RRiBbitUtil.createRequestResponseBusForLocalUse(listenerFactory(), false);
    }
}