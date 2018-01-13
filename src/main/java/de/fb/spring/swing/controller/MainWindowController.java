package de.fb.spring.swing.controller;

import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Controller logic for the MainWindow view class.
 * 
 * @author ibragim
 * 
 */
@Component("mainWindowController")
@SuppressWarnings("unused")
public class MainWindowController {

    private static Logger logger = LoggerFactory.getLogger(MainWindowController.class);

    @Autowired
    private ApplicationContext appContext;

    public MainWindowController() {

    }

    public Boolean handleAppRequestExitEvent(final ActionEvent event) {

        /*
         * TODO: check for pending and ongoing operations and deny exit (perhaps call back to view)
         * as long as there any active tasks in the queue.
         */
        return Boolean.TRUE;
    }

    public void handleAppExitEvent(final ActionEvent event) {

        // TODO: add any necessary appication context and resources cleanup code
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
        logger.info(selectedItem.toString());
    }
}
