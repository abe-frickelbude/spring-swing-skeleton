package de.fb.spring.swing.controller;

import java.awt.event.ActionEvent;
import org.rribbit.Listener;
import org.rribbit.RequestResponseBus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import de.fb.spring.swing.annotations.Log;
import de.fb.spring.swing.view.MainWindow;

/**
 * Controller logic for the MainWindow view class. Events from various view components are received (and conversely,
 * sent to the view to update it) via the event bus.
 * 
 * @author ibragim
 * 
 */
@Controller(value = "mainWindowController")
public class MainWindowController {

    public static final String DB_COMBO_BOX_UPDATE_EVENT = "updateDbBox";

    @Log
    private static Logger logger;

    @Autowired
    private RequestResponseBus eventBus;

    @Autowired
    private ApplicationContext appContext;

    public MainWindowController() {

    }

    @Listener(hint = MainWindow.REQUEST_EXIT_EVENT)
    public Boolean handleAppRequestExitEvent(@SuppressWarnings("unused") final ActionEvent event) {

        /*
         * TODO: check for pending and ongoing operations and deny exit (perhaps call back to view)
         * as long as there any active tasks in the queue.
         */
        return Boolean.TRUE;
    }

    @Listener(hint = MainWindow.EXIT_EVENT)
    public void handleAppExitEvent(@SuppressWarnings("unused") final ActionEvent event) {

        // TODO: add any necessary appication context and resources cleanup code
        System.exit(0);
    }

    @Listener(hint = MainWindow.CONNECT_SOURCE_EVENT)
    public void handleConnectDbEvent(@SuppressWarnings("unused") final ActionEvent event) {
        logger.info("Connect button!");
        // TODO
    }

    @Listener(hint = MainWindow.START_PROCESSING_EVENT)
    public void handleStartAggEvent(@SuppressWarnings("unused") final ActionEvent event) {
        logger.info("Start button!");
        // TODO
    }

    @Listener(hint = MainWindow.STOP_PROCESSING_EVENT)
    public void handleStopAggEvent(@SuppressWarnings("unused") final ActionEvent event) {
        logger.info("Stop button!");
        // TODO
    }

    @Listener(hint = MainWindow.SOURCE_SELECTION_EVENT)
    public void handleDatabaseSelectionEvent(final String selectedItem) {

        logger.info(selectedItem.toString());
        // TODO: interface to database statistics service layer
    }

    // test!
    // public void updateDatabaseSelectionBox() {
    //
    // List<String> dbNames = new ArrayList<String>();
    // for (int i = 0; i < 8; i++) {
    // dbNames.add(new String("dwrun200" + i));
    // }
    //
    // eventBus.send(DB_COMBO_BOX_UPDATE_EVENT, dbNames);
    // }
}
