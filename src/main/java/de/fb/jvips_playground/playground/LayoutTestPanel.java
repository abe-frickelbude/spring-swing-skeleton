package de.fb.jvips_playground.playground;

import javax.swing.*;
import java.awt.*;

public class LayoutTestPanel {

    public JPanel mainPanel;
    private JButton openFileButton;
    private JSpinner overlaySpinnerX;
    private JSpinner overlaySpinnerY;
    private JButton applyButton;
    private JSpinner scaleWidthSpinner;
    private JSpinner scaleHeightSpinner;
    private JButton exitButton;
    private JTextField watermarkTextField;

    public LayoutTestPanel() {

    }

    public static void main(String[] args) {

        var frame = new JFrame();
        var layoutTestPanel = new LayoutTestPanel();

        frame.setTitle("Layout playground");
        frame.setContentPane(layoutTestPanel.mainPanel);

        //window.getContentPane().add(layoutTestPanel.mainPanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(100,100,500,500);
        frame.setVisible(true);

    }
}
