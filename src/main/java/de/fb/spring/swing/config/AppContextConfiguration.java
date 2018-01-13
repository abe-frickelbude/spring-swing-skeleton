package de.fb.spring.swing.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import de.fb.spring.swing.util.LoggerPostProcessor;

@Configuration
@ComponentScan(basePackages = "de.fb.spring.swing")
public class AppContextConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AppContextConfiguration.class);

    @Bean
    public LoggerPostProcessor loggerPostProcessor() {
        LoggerPostProcessor bean = new LoggerPostProcessor();
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
