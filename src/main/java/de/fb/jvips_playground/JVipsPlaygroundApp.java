package de.fb.jvips_playground;

import com.bulenkov.darcula.DarculaLaf;
import de.fb.jvips_playground.view.LogWindow;
import de.fb.jvips_playground.view.MainWindow;
import de.fb.jvips_playground.view.UILayouter;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.swing.*;
import java.awt.*;

import static org.burningwave.core.assembler.StaticComponentContainer.Modules;

// main application class
public class JVipsPlaygroundApp {

    private static final Logger log = LoggerFactory.getLogger(JVipsPlaygroundApp.class);

    // singleton application context instance
    private static ApplicationContext appContext;

    public static void main(final String[] args) {

        // hack Java >= 9 reflection without having to pass "add-opens" JVM cli parameters
        // needed mainly by the Darcula LAF
        Modules.exportAllToAll();

        configureLogging();

        // Swing "platform look and feel" has to be set PRIOR to any component initialization
        // or it will have no effect!
        configureSwingLookAndFeel();
        initializeApp(args);
    }

    private static void initializeApp(final String[] args) {
        /*
         * The section below might now be completely self-explanatory, so:
         * Basically the >run< sequence actually consists of two steps - the first one has to initialize the Spring
         * app context along with all SpringBoot magic, and the second one then initializes and displays the Swing UI.
         * For this to work, we need to retrieve the app context from the initialized SpringApplication instance, hence
         * the line below.
         */
        try {

            appContext = Micronaut.build(args)
                .eagerInitSingletons(true)
                .eagerInitConfiguration(true)
                .mainClass(JVipsPlaygroundApp.class)
                .start();

            EventQueue.invokeLater(() -> {
                try {
                    // show the UI windows
                    final MainWindow mainWindow = appContext.getBean(MainWindow.class);
                    final LogWindow logWindow = appContext.getBean(LogWindow.class);
                    //final ControlWindow controlWindow = appContext.getBean(ControlWindow.class);

                    UILayouter uiLayouter = appContext.getBean(UILayouter.class);

                    uiLayouter.layoutWindows(false, 0.7f, 0.6f);
                    mainWindow.setVisible(true);
                    logWindow.setVisible(true);
                    //controlWindow.setVisible(true);

                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    System.exit(-1);
                }
            });
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            System.exit(-1);
        }
    }

    private static void configureLogging() {
        // route java.util.logging through SLF4J
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static void configureSwingLookAndFeel() {
        try {
            UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo(DarculaLaf.NAME, DarculaLaf.class.getName()));
            UIManager.setLookAndFeel(DarculaLaf.class.getName());

        } catch (Exception ex) {
            // Note: this is not an unrecoverable exception -
            // the application will just use the default look and feel!
            log.error(ex.getMessage(), ex);
        }
    }
}
