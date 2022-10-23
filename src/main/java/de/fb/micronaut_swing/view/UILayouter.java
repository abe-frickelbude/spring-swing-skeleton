package de.fb.micronaut_swing.view;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

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
    private static final int HORIZONTAL_MARGIN = 10;

    /*
     * gap between top of the windows and the top edge of the screen
     */
    private static final int TOP_MARGIN = 10;

    /*
     * gap between bottom edges of the windows and the bottom edge of the screen, so that the windows don't accidentally
     * overlap the OS taskbar and whatever else may be at the bottom of the screen.
     */
    private static final int BOTTOM_MARGIN = 100;

    private final MainWindow mainWindow;
    private final LogWindow secondaryWindow;

    @Inject
    public UILayouter(final MainWindow mainWindow, final LogWindow secondaryWindow) {
        this.mainWindow = mainWindow;
        this.secondaryWindow = secondaryWindow;
    }

    /**
     * Overall strategy for window layout:
     *
     * - for a single-display setup ,position both windows side by side with default size ratio is 1:1, with some extra
     * space between them to make it look a little nicer.
     *
     * - If more than one display is available: -> Set the default main window dimensions to be {MAIN_WINDOW_SIZE} %
     * (rounded to next integers) of the "main" display resolution and center it there. Then move the secondary window
     * to the second display and maximize it.
     *
     */
    public void layoutWindows(final boolean useMultipleDisplays) {

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = environment.getScreenDevices();

        // debug
        log.debug("Available displays:");
        for (GraphicsDevice device : devices) {
            log.debug("Display {} -> {} x {}", device.getIDstring(),
                device.getDisplayMode().getWidth(),
                device.getDisplayMode().getHeight());
        }

        if (devices.length > 1 && useMultipleDisplays == true) {

            // we have multiple displays available
            GraphicsDevice defaultDevice = environment.getDefaultScreenDevice();

            int windowWidth = Math.round(defaultDevice.getDisplayMode().getWidth() * MAIN_WINDOW_SIZE);
            int windowHeight = Math.round(defaultDevice.getDisplayMode().getHeight() * MAIN_WINDOW_SIZE);

            int windowX = (defaultDevice.getDisplayMode().getWidth() - windowWidth) / 2;
            int windowY = (defaultDevice.getDisplayMode().getHeight() - windowHeight) / 2;

            Rectangle mainBounds = new Rectangle(windowX, windowY, windowWidth, windowHeight);
            mainWindow.setBounds(mainBounds);

            secondaryWindow.setBounds(devices[1].getDefaultConfiguration().getBounds());
            secondaryWindow.setExtendedState(secondaryWindow.getExtendedState() | Frame.MAXIMIZED_BOTH);

        } else {

            // we only have one display
            GraphicsDevice defaultDevice = environment.getDefaultScreenDevice();

            int displayWidth = defaultDevice.getDisplayMode().getWidth();
            int displayHeight = defaultDevice.getDisplayMode().getHeight();

            int windowWidth = displayWidth / 2 - HORIZONTAL_MARGIN;
            int windowHeight = displayHeight - (HORIZONTAL_MARGIN + BOTTOM_MARGIN);

            Rectangle mainBounds = new Rectangle(HORIZONTAL_MARGIN, TOP_MARGIN, windowWidth, windowHeight);
            mainWindow.setBounds(mainBounds);

            Rectangle auxBounds = new Rectangle(displayWidth / 2 + HORIZONTAL_MARGIN,
                TOP_MARGIN,
                windowWidth - HORIZONTAL_MARGIN,
                windowHeight);
            secondaryWindow.setBounds(auxBounds);
        }
    }
}
