package de.fb.micronaut_swing;

import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Main application class, responsible for instantiating the Spring application context, along
 * with logging subsystem initialization etc.
 *
 * @author Ibragim Kuliev
 */
public class MicronautSwingTemplate {

    // global application object
    private static MicronautSwingTemplateApp app;

    public MicronautSwingTemplate() {
    }

    public static void main(final String[] arguments) {
        configureLogging();
        initAppContext(arguments);
    }

    private static void initAppContext(final String[] arguments) {
        app = new MicronautSwingTemplateApp(arguments);
        app.run();
    }

    private static void configureLogging() {
        // route java.util.logging through SLF4J
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
