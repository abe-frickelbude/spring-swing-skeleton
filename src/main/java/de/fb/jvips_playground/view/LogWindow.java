package de.fb.jvips_playground.view;

import de.fb.jvips_playground.annotations.SwingView;
import de.fb.jvips_playground.view.ansi.ConsoleLogPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@SwingView
public class LogWindow extends JFrame {

    private final JPanel contentPane;
    private final ConsoleLogPane consoleLogPane;

    public LogWindow() {
        setTitle("Log console");

        // prevent this window from closing
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 692, 765);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        consoleLogPane = new ConsoleLogPane();

        // testing
        consoleLogPane.setColorScheme("/ansi_color_schemes/monokai");
        consoleLogPane.setUseDefaultLafColors(true);
        consoleLogPane.setCaptureStandardStreams(true);

        consoleLogPane.setFont(new Font("Verdana", Font.PLAIN, 10));
        consoleLogPane.setMargin(new Insets(5, 5, 5, 5));
        consoleLogPane.setVerifyInputWhenFocusTarget(false);
        consoleLogPane.setEditable(false);

        final JScrollPane scrollPane = new JScrollPane(consoleLogPane);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }
}
