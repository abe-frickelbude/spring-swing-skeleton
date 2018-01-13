package de.fb.spring.swing;

import java.awt.EventQueue;
import java.io.BufferedOutputStream;
import java.io.PrintStream;
import javax.swing.UIManager;
import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.bulenkov.darcula.DarculaLaf;
import de.fb.spring.swing.config.AppContextConfiguration;
import de.fb.spring.swing.view.LogWindow;
import de.fb.spring.swing.view.MainWindow;

/**
 * This class essentially ties together Spring application context initialization and any auxiliary initialization of
 * Swing etc, and is ultimately responsible for setting up the Swing GUI.
 * 
 * @author Ibragim Kuliev
 *
 */
public class SpringSwingTemplateApp {

    private static final Logger log = LoggerFactory.getLogger(SpringSwingTemplateApp.class);

    private final AnnotationConfigApplicationContext appContext;

    /**
     * 
     * @param arguments
     */
    public SpringSwingTemplateApp(final String[] arguments) {
        configureSwingLookAndFeel();
        appContext = new AnnotationConfigApplicationContext(AppContextConfiguration.class);
        appContext.registerShutdownHook();
    }

    public void run() {

        EventQueue.invokeLater(new Runnable() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void run() {
                try {

                    // show the UI windows
                    MainWindow mainWindow = appContext.getBean(MainWindow.class);
                    LogWindow logWindow = appContext.getBean(LogWindow.class);

                    captureSystemMessageStreams(logWindow);
                    mainWindow.setVisible(true);
                    logWindow.setVisible(true);

                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
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
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(new DarculaLaf());
        } catch (Exception ex) {

            // Note: this is not an unrecoverable exception - the application will just use the default look and feel!
            log.error(ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("resource")
    private void captureSystemMessageStreams(final LogWindow logWindow) {

        // split System.out and System.err and direct the cloned streams to the logging area
        PrintStream sysout = System.out;
        TeeOutputStream sysoutSplitter = new TeeOutputStream(sysout, new BufferedOutputStream(logWindow.getLogSysOutStream()));
        PrintStream sysoutTeeWrapper = new PrintStream(sysoutSplitter, true);
        System.setOut(sysoutTeeWrapper);

        PrintStream syserr = System.err;
        TeeOutputStream syserrSplitter = new TeeOutputStream(syserr, new BufferedOutputStream(logWindow.getLogSysErrStream()));
        PrintStream syserrTeeWrapper = new PrintStream(syserrSplitter, true);
        System.setErr(syserrTeeWrapper);

        log.info("Log init completed!");
    }
}
