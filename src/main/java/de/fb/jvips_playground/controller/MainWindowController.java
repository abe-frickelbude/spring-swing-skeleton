package de.fb.jvips_playground.controller;

import de.fb.jvips_playground.view.MainWindow;
import io.micronaut.context.ApplicationContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

@Singleton
@SuppressWarnings("unused")
public class MainWindowController {

    private static final Logger log = LoggerFactory.getLogger(MainWindowController.class);

    private final ApplicationContext appContext;
    private  MainWindow mainWindow;

    @Inject
    public MainWindowController(final ApplicationContext appContext) {
        this.appContext = appContext;
    }

    public void setWindow(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setReferenceImage(final BufferedImage image) {
        mainWindow.setReferenceImage(image);
    }

    public Boolean requestAppExit() {
        /*
         * TODO: check for pending and ongoing operations and deny exit (perhaps call back to view)
         * as long as there any active tasks in the queue.
         */
        return Boolean.TRUE;
    }

    public void exitApp() {
        // EXTEND IF NECESSARY add any necessary application context and resources cleanup code
        System.exit(0);
    }

}
