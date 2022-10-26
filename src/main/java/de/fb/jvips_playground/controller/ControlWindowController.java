package de.fb.jvips_playground.controller;

import io.micronaut.context.ApplicationContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ControlWindowController {

    private static final Logger logger = LoggerFactory.getLogger(MainWindowController.class);

    private final ApplicationContext appContext;

    @Inject
    public ControlWindowController(ApplicationContext appContext) {
        this.appContext = appContext;
    }
}
