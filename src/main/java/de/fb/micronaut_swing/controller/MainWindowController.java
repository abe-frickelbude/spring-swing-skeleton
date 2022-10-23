package de.fb.micronaut_swing.controller;

import java.awt.event.ActionEvent;

import io.micronaut.context.ApplicationContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller logic for the MainWindow view class.
 *
 * @author ibragim
 *
 */
@Singleton
@SuppressWarnings("unused")
public class MainWindowController {

    private static final Logger logger = LoggerFactory.getLogger(MainWindowController.class);

    private final ApplicationContext appContext;

    @Inject
    public MainWindowController(final ApplicationContext appContext) {
        this.appContext = appContext;
    }

    public Boolean handleAppRequestExitEvent(final ActionEvent event) {
        /*
         * TODO: check for pending and ongoing operations and deny exit (perhaps call back to view)
         * as long as there any active tasks in the queue.
         */
        return Boolean.TRUE;
    }

    public void handleAppExitEvent(final ActionEvent event) {
        // TODO: add any necessary application context and resources cleanup code
        System.exit(0);
    }

    public void handleConnectDbEvent(final ActionEvent event) {
        logger.info("Connect button!");
        // TODO
    }

    public void handleStartAggEvent(final ActionEvent event) {
        logger.info("Start button!");
        // TODO
    }

    public void handleStopAggEvent(final ActionEvent event) {
        logger.info("Stop button!");
        // TODO
    }

    public void handleDatabaseSelectionEvent(final String selectedItem) {
        logger.info(selectedItem);
    }
}
