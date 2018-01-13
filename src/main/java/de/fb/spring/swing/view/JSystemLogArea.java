package de.fb.spring.swing.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import org.apache.commons.lang3.StringUtils;

/**
 * A simple log message display derived from JTextArea which provides two stream endpoints that can be used
 * to dynamically capture the System.out and System.err streams, thereby redirecting console messages into the text
 * area.
 * 
 * This component should be wrapped in a JScrollPane to provide text scrolling.
 * 
 * @author ibragim
 * 
 */
public final class JSystemLogArea extends JTextArea {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Small adapter class that automatically appends its content to the parent JTextArea upon each flush().
     * 
     * @author ibragim
     * 
     */
    private static class MessageOutputStream extends ByteArrayOutputStream {

        private JSystemLogArea logArea;

        public MessageOutputStream(final JSystemLogArea logArea, final int initialSize) {
            super(initialSize);
            this.logArea = logArea;
        }

        @Override
        public void flush() throws IOException {
            this.logArea.append(super.toString());
            super.reset();
        }
    }

    private int maxLines = 100;
    private int scrollBatchSize = 5;

    private MessageOutputStream stdOutStream;
    private MessageOutputStream stdErrStream;

    /**
     * 
     */
    public JSystemLogArea() {
        super();
        stdOutStream = new MessageOutputStream(this, BUFFER_SIZE);
        stdErrStream = new MessageOutputStream(this, BUFFER_SIZE);
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(final int maxLines) {
        this.maxLines = maxLines;
        clear();
    }

    public int getScrollBatchSize() {
        return scrollBatchSize;
    }

    public void setScrollBatchSize(final int size) {
        this.scrollBatchSize = size;
    }

    /**
     * Return the internal standard output stream object.
     * 
     * @return
     */
    public OutputStream getStdOutStream() {
        return stdOutStream;
    }

    /**
     * Return the internal standard error stream object.
     * 
     * @return
     */
    public OutputStream getStdErrStream() {
        return stdErrStream;
    }

    public void clear() {
        super.setText(StringUtils.EMPTY);
    }

    @Override
    public void append(String text) {

        if (getLineCount() > maxLines) {
            clear();
        }
        super.append(text);

        // auto-scroll text after scrollBatchSize new lines are appended.
        if (getLineCount() % scrollBatchSize == 0) {
            setCaretPosition(getDocument().getLength());
        }
    }
}
