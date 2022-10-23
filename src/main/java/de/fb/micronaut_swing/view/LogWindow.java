package de.fb.micronaut_swing.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.io.OutputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import de.fb.micronaut_swing.annotations.SwingView;

@SwingView
public class LogWindow extends JFrame {

    private final JPanel contentPane;
    private final JSystemLogArea logMessageArea;

    /**
     * Create the frame.
     */
    public LogWindow() {
        setTitle("Log console");

        // prevent this window from closing
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 692, 765);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        logMessageArea = new JSystemLogArea();

        logMessageArea.setRows(40);
        logMessageArea.setColumns(30);
        logMessageArea.setMaxLines(25000);
        logMessageArea.setWrapStyleWord(true);
        logMessageArea.setMargin(new Insets(5, 5, 5, 5));
        logMessageArea.setVerifyInputWhenFocusTarget(false);
        logMessageArea.setTabSize(4);
        logMessageArea.setLineWrap(true);
        logMessageArea.setFont(new Font("Verdana", Font.PLAIN, 11));
        logMessageArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(logMessageArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    public final OutputStream getLogSysOutStream() {
        return logMessageArea.getStdOutStream();
    }

    public final OutputStream getLogSysErrStream() {
        return logMessageArea.getStdErrStream();
    }
}
