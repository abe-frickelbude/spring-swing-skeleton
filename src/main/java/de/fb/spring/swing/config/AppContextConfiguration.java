package de.fb.spring.swing.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "de.fb.spring.swing")
public class AppContextConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AppContextConfiguration.class);

}
