package de.fb.jvips_playground.view;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import io.micronaut.context.ApplicationContext;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class UILayouter {

    private static final Logger log = LoggerFactory.getLogger(UILayouter.class);

    // window size relative to primary display dimensions, when in multi-display mode
    private static final float MAIN_WINDOW_SIZE = 0.65f;

    // gap between windows when side-by-side on a single display
    private static final int HORIZONTAL_GAP = -5;

    private static final int VERTICAL_GAP = 2;

    private static final int LEFT_MARGIN = 10;
    private static final int RIGHT_MARGIN = 10;

    // gap between top of the windows and the top edge of the screen
    private static final int TOP_MARGIN = 10;

    /*
     * gap between bottom edges of the windows and the bottom edge of the screen, so that the windows don't accidentally
     * overlap the OS taskbar and whatever else may be at the bottom of the screen.
     */
    private static final int BOTTOM_MARGIN = 70;

    private final ApplicationContext appContext;

    @Inject
    public UILayouter(final ApplicationContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Overall strategy for window layout:
     *
     * - for a single-display setup ,position the windows side by side with horizontal size ratio controlled by the
     * widthRatio value,
     * with some extra space between them to make it look a little nicer. The log and control windows are placed on the
     * right side of the main window, with vertical space split between them using the heightRatio value
     *
     * TODO: this section is outdated!!
     *
     * - If more than one display is available: -> Set the default main window dimensions to be {MAIN_WINDOW_SIZE} %
     * (rounded to next integers) of the "main" display resolution and center it there. Then move the secondary window
     * to the second display and maximize it.
     *
     */
    public void layoutWindows(final boolean useMultipleDisplays, final float widthRatio, final float heightRatio) {

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = environment.getScreenDevices();

        // debug
        log.info("Available displays:");
        for (GraphicsDevice device : devices) {
            log.info("Display {} -> {} x {}", device.getIDstring(),
                device.getDisplayMode().getWidth(),
                device.getDisplayMode().getHeight());
        }

        if (devices.length > 1 && useMultipleDisplays) {
            layoutForDualDisplays();
        } else {
            layoutForSingleDisplay(widthRatio, heightRatio);
        }
    }

    private void layoutForDualDisplays() {

        // we have multiple displays available
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = environment.getScreenDevices();
        GraphicsDevice defaultDevice = environment.getDefaultScreenDevice();

        final MainWindow mainWindow = appContext.getBean(MainWindow.class);
        final LogWindow logWindow = appContext.getBean(LogWindow.class);
        //final ControlWindow controlWindow = appContext.getBean(ControlWindow.class);

        int windowWidth = Math.round(defaultDevice.getDisplayMode().getWidth() * MAIN_WINDOW_SIZE);
        int windowHeight = Math.round(defaultDevice.getDisplayMode().getHeight() * MAIN_WINDOW_SIZE);

        int windowX = (defaultDevice.getDisplayMode().getWidth() - windowWidth) / 2;
        int windowY = (defaultDevice.getDisplayMode().getHeight() - windowHeight) / 2;

        Rectangle mainBounds = new Rectangle(windowX, windowY, windowWidth, windowHeight);
        mainWindow.setBounds(mainBounds);

        logWindow.setBounds(devices[1].getDefaultConfiguration().getBounds());
        logWindow.setExtendedState(logWindow.getExtendedState() | Frame.MAXIMIZED_BOTH);
    }

    // we only have one display
    private void layoutForSingleDisplay(final float widthRatio, final float heightRatio) {

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultDevice = environment.getDefaultScreenDevice();

        final MainWindow mainWindow = appContext.getBean(MainWindow.class);
        final LogWindow logWindow = appContext.getBean(LogWindow.class);
        //final ControlWindow controlWindow = appContext.getBean(ControlWindow.class);

        // precalculate left/right/top/bottom margins into the width/height of the layout area
        int displayWidth = defaultDevice.getDisplayMode().getWidth() - (LEFT_MARGIN + RIGHT_MARGIN);
        int displayHeight = defaultDevice.getDisplayMode().getHeight() - (TOP_MARGIN + BOTTOM_MARGIN);

        Rectangle mainBounds = new Rectangle(
            LEFT_MARGIN,
            TOP_MARGIN,
            Math.round(displayWidth * widthRatio),
            displayHeight);

        mainWindow.setBounds(mainBounds);

        Rectangle controlWindowBounds = new Rectangle(
            Math.round(displayWidth * widthRatio) + (HORIZONTAL_GAP + LEFT_MARGIN),
            TOP_MARGIN,
            Math.round(displayWidth * (1.0f - widthRatio)) - (HORIZONTAL_GAP + RIGHT_MARGIN),
            Math.round(displayHeight * heightRatio));

        //controlWindow.setBounds(controlWindowBounds);

        Rectangle logWindowBounds = new Rectangle(
            Math.round(displayWidth * widthRatio) + (HORIZONTAL_GAP + LEFT_MARGIN),
            Math.round(displayHeight * heightRatio) + (VERTICAL_GAP + TOP_MARGIN),
            Math.round(displayWidth * (1.0f - widthRatio)) - (HORIZONTAL_GAP + RIGHT_MARGIN),
            Math.round(displayHeight * (1.0f - heightRatio)) - VERTICAL_GAP);

        logWindow.setBounds(logWindowBounds);
    }

    @PostConstruct
    private void init() {
        registerWindowStateChangeEvents();
    }

    /**
     * Synchronizes some primary window state changes with the aux windows, e.g. brings everything into foreground
     * when the main window gains focus, or minimizes everything when the main window is minimized.
     */
    private void registerWindowStateChangeEvents() {

        final MainWindow mainWindow = appContext.getBean(MainWindow.class);
        final LogWindow logWindow = appContext.getBean(LogWindow.class);
        //final ControlWindow controlWindow = appContext.getBean(ControlWindow.class);

        mainWindow.addWindowFocusListener(new WindowAdapter() {

            @Override
            public void windowGainedFocus(final WindowEvent event) {

                if (mainWindow.isShowing()) {
                    logWindow.toFront();
                    //controlWindow.toFront();
                }
            }
        });

        mainWindow.addWindowListener(new WindowAdapter() {

            @Override
            public void windowIconified(final WindowEvent e) {
                logWindow.setExtendedState(Frame.ICONIFIED);
                //controlWindow.setExtendedState(Frame.ICONIFIED);
            }

            @Override
            public void windowDeiconified(final WindowEvent e) {
                logWindow.setExtendedState(Frame.NORMAL);
                //controlWindow.setExtendedState(Frame.NORMAL);
                logWindow.toFront();
                //controlWindow.toFront();
            }
        });
    }
}
