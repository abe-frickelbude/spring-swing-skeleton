package de.fb.jvips_playground.view.ansi;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A text display pane that interprets ANSI escape codes in the incoming text and automatically colorizes the text
 * appropriately. Supports user-defined terminal color schemes.
 *
 * <p>
 * ANSI code processing can be toggled via setColorsEnabled() - if disabled, all escape sequences are stripped and the text
 * appended as-is.
 * </p>
 *
 * <p>
 * Currently supports "basic" ANSI escape codes for the 16 foreground and 16 background colors, respectively, via a
 * user defined color scheme. Defaults to Xterm color scheme if not explicitly set via setColorScheme().
 * </p>
 *
 * <p>
 * All methods that might "hog" the EDT are already properly isolated via SwingUtilities.invokeLater() barrier.
 * </p>
 *
 */
public class AnsiTextPane extends JTextPane {

    private static final Logger log = LoggerFactory.getLogger(AnsiTextPane.class);

    private final AnsiCodeProcessor ansiCodeProcessor;

    public AnsiTextPane() {
        super();
        ansiCodeProcessor = new AnsiCodeProcessor();
        ansiCodeProcessor.setColorScheme("/ansi_color_schemes/xterm");
    }

    public void append(final String text) {
        /*
         * Note: for some reason, this also automatically resolves the missing auto-scroll issue on [potentially]
         * present surrounding JScrollPane!
         */
        SwingUtilities.invokeLater(() -> {
            ansiCodeProcessor.appendText(text, getDocument());
        });
    }

    public void clear() {
        SwingUtilities.invokeLater(() -> {
            try {
                getDocument().remove(0, getDocument().getLength());
            } catch (BadLocationException ex) {
                log.warn(ex.getMessage());
            }
        });
    }

    public int getContentLength() {
        return getDocument().getLength();
    }

    public void setColorScheme(final String schemeName) {
        ansiCodeProcessor.setColorScheme(schemeName);
    }

    public void setColorsEnabled(final boolean colorsEnabled) {
        ansiCodeProcessor.setColorsEnabled(colorsEnabled);
    }

    public void setUseDefaultLafColors(final boolean useDefaultColors) {
        if (useDefaultColors) {
            ansiCodeProcessor.setDefaultColors(getForeground(), getBackground());
        } else {
            ansiCodeProcessor.resetDefaultColors();
        }
    }
}
