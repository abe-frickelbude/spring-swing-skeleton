package de.fb.jvips_playground.view;

import de.fb.jvips_playground.annotations.SwingView;
import de.fb.jvips_playground.view.controls.VipsControlPanel;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

@SwingView
public class ControlWindow extends JFrame {

    private JPanel contentPane;
    private VipsControlPanel controlPanel;

    public ControlWindow() {
        initUI();
    }

    // only GUI & glue code here, the control logic is  in the ControlWindowController
    private void initUI() {

        setTitle("Controls");

        // prevent this window from closing
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 692, 765);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        controlPanel = new VipsControlPanel();
        contentPane.add(controlPanel.$$$getRootComponent$$$(), BorderLayout.CENTER);
    }

    private void connectEventHandlers() {

    }
}
