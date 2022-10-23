package de.fb.micronaut_swing;

import java.awt.EventQueue;
import java.io.BufferedOutputStream;
import java.io.PrintStream;
import javax.swing.UIManager;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.fb.micronaut_swing.view.LogWindow;
import de.fb.micronaut_swing.view.MainWindow;
import de.fb.micronaut_swing.view.UILayouter;

/**
 * This class essentially ties together Spring application context initialization and any auxiliary initialization of
 * Swing etc, and is ultimately responsible for setting up the Swing GUI.
 *
 * @author Ibragim Kuliev
 */
public class MicronautSwingTemplateApp {

    private static final Logger log = LoggerFactory.getLogger(MicronautSwingTemplateApp.class);

    private final ApplicationContext appContext;

    public MicronautSwingTemplateApp(final String[] arguments) {
        configureSwingLookAndFeel();

        appContext = Micronaut.build(arguments)
            .eagerInitSingletons(true)
            .eagerInitConfiguration(true)
            .mainClass(MicronautSwingTemplateApp.class)
            .start();
    }

    public void run() {
        EventQueue.invokeLater(() -> {
            try {

                // show the UI windows
                MainWindow mainWindow = appContext.getBean(MainWindow.class);
                LogWindow logWindow = appContext.getBean(LogWindow.class);
                UILayouter uiLayouter = appContext.getBean(UILayouter.class);

                captureSystemMessageStreams(logWindow);
                uiLayouter.layoutWindows(false);

                mainWindow.setVisible(true);
                logWindow.setVisible(true);

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        });
    }

    public void shutdown() {
        // System.exit(0);
    }

    private void configureSwingLookAndFeel() {
        // Swing "platform look and feel" has to be set PRIOR to any component initialization,
        // otherwise it will have no effect!
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Note: this is not an unrecoverable exception - the application will just use the default look and feel!
            log.error(ex.getMessage(), ex);
        }
    }

    private void captureSystemMessageStreams(final LogWindow logWindow) {

        // split System.out and System.err and direct the cloned streams to the logging area
        PrintStream sysOut = System.out;
        TeeOutputStream sysOutSplitter = new TeeOutputStream(sysOut,
            new BufferedOutputStream(logWindow.getLogSysOutStream()));

        PrintStream sysOutTeeWrapper = new PrintStream(sysOutSplitter, true);
        System.setOut(sysOutTeeWrapper);

        PrintStream sysErr = System.err;
        TeeOutputStream sysErrSplitter = new TeeOutputStream(sysErr,
            new BufferedOutputStream(logWindow.getLogSysErrStream()));

        PrintStream sysErrTeeWrapper = new PrintStream(sysErrSplitter, true);
        System.setErr(sysErrTeeWrapper);

        log.info("Log init completed!");
    }
}
