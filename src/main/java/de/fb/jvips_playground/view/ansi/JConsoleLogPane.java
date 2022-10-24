package de.fb.jvips_playground.view.ansi;

import java.io.PrintStream;
import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.fb.jvips_playground.util.Constants;

/**
 * Extends {@linkplain JAnsiTextPane} with standard IO stream capturing. This allows one to mirror all System.out /
 * System.err stream output (and by extension also anything that uses them, e.g. every major logging framework)
 *
 * @author Ibragim Kuliev
 *
 */
public class JConsoleLogPane extends JAnsiTextPane {

    private static final Logger log = LoggerFactory.getLogger(JConsoleLogPane.class);

    private static final int DEFAULT_CAPTURE_BUFFER_SIZE = 1024; // 1KB
    private static final int MAX_CONTENT_LENGTH = 2 * Constants.ONE_MEGABYTE;

    private int captureBufferSize;
    private boolean captureStandardStreams;

    /*
     * maximum length of string content before the pane self-clears to make room for
     * more content. This is a trade-off between number of lines kept in the view and heap consumption.
     */
    private int maxContentLength;

    private PrintStream stdOutStream;
    private PrintStream stdErrStream;

    public JConsoleLogPane() {

        super();
        captureBufferSize = DEFAULT_CAPTURE_BUFFER_SIZE;
        maxContentLength = MAX_CONTENT_LENGTH;
        captureStandardStreams = false;
    }

    public void setCaptureStandardStreams(final boolean captureStandardStreams) {
        this.captureStandardStreams = captureStandardStreams;
        captureStandardStreams(captureStandardStreams);
    }

    public boolean isStreamCaptureEnabled() {
        return captureStandardStreams;
    }

    public void setCaptureBufferSize(final int captureBufferSize) {
        if (captureBufferSize > 0) {
            this.captureBufferSize = captureBufferSize;
        } else {
            this.captureBufferSize = DEFAULT_CAPTURE_BUFFER_SIZE;
        }
    }

    public void setMaxContentLength(final int maxContentLength) {
        if (maxContentLength > 0) {
            this.maxContentLength = maxContentLength;
        } else {
            this.maxContentLength = MAX_CONTENT_LENGTH;
        }
    }

    @Override
    public void append(final String text) {

        if (getDocument().getLength() > maxContentLength) {
            super.clear();
        }
        super.append(text);
    }

    @SuppressWarnings("resource")
    private void captureStandardStreams(final boolean capture) {

        if (capture) {
            // split System.out and System.err and direct the cloned streams to the logging area
            PrintStream sysout = System.out;
            stdOutStream = System.out;

            TeeOutputStream sysoutSplitter = new TeeOutputStream(sysout,
                new MessageOutputStream(captureBufferSize, this::append));

            PrintStream sysoutTeeWrapper = new PrintStream(sysoutSplitter, true);
            System.setOut(sysoutTeeWrapper);

            PrintStream syserr = System.err;
            stdErrStream = System.err;

            TeeOutputStream syserrSplitter = new TeeOutputStream(syserr,
                new MessageOutputStream(captureBufferSize, this::append));

            PrintStream syserrTeeWrapper = new PrintStream(syserrSplitter, true);
            System.setErr(syserrTeeWrapper);

            log.info("System.out and System.err mirrored!");

        } else {
            // restore original system streams
            if (stdOutStream != null) {
                System.setOut(stdOutStream);
            }
            if (stdErrStream != null) {
                System.setErr(stdErrStream);
            }
            log.info("System.out and System.err restored!");
        }
    }
}
