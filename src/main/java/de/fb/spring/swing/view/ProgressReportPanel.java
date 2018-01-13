package de.fb.spring.swing.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

/**
 * This class provides a simple progress display for up to N "tasks", showing a symbolic name and a progress bar for
 * each task.
 * 
 * @author ibragim
 * 
 */
public final class ProgressReportPanel extends JPanel {

    private static final int MIN_BAR_VALUE = 0;
    private static final int MAX_BAR_VALUE = 100;

    private int numTasks;
    private Insets elementInsets;
    private Color progressBarForeground;

    private JPanel elementPanel;
    private final List<JLabel> taskNames;
    private final List<JProgressBar> taskProgressBars;

    /**
     * Create the panel.
     */
    public ProgressReportPanel() {
        super();

        taskNames = new ArrayList<>();
        taskProgressBars = new ArrayList<>();

        // defaults
        setProgressBarForeground(new Color(0, 0, 128));
        setGlobalInsets(new Insets(5, 5, 5, 5));
        setNumTasks(1);
    }

    public int getNumTasks() {
        return numTasks;
    }

    public void setNumTasks(final int numTasks) {
        this.numTasks = numTasks;
    }

    public Insets getGlobalInsets() {
        return elementInsets;
    }

    public void setGlobalInsets(final Insets insets) {
        this.elementInsets = insets;
    }

    public Color getProgressBarForeground() {
        return progressBarForeground;
    }

    public void setProgressBarForeground(final Color foreground) {
        this.progressBarForeground = foreground;
    }

    public void initUI() {

        setBackground(new Color(240, 240, 240));

        // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // setLayout(new FlowLayout());
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.SOUTH);

        elementPanel = new JPanel();
        elementPanel.setAutoscrolls(true);
        scrollPane.setViewportView(elementPanel);

        createReportWidgets();
    }

    /**
     * Set task name and task progress indicator for the selected task.
     * 
     * @param taskIndex
     *        selected task index, must be in [0...numTasks-1]
     * @param taskProgress
     *        progress value, must be in [0...100]
     * @param taskName
     *        name of the task, can be an arbitrary string.
     */
    public void setTaskInfo(final int taskIndex, final int taskProgress, final String taskName) {

        if ((taskIndex < 0) || (taskIndex > numTasks - 1)) {
            throw new IllegalArgumentException("Invalid task index!");
        }

        taskNames.get(taskIndex).setText(taskName);
        taskProgressBars.get(taskIndex).setValue(taskProgress);
    }

    /**
     * Populate inner element panel with a set of N label/progress bar pairs, according to the numReports property.
     */
    private void createReportWidgets() {

        GridBagLayout panelLayout = new GridBagLayout();

        panelLayout.columnWidths = new int[] {
            0, 0, 0
        };

        panelLayout.columnWeights = new double[] {
            0.0, 0.0, Double.MIN_VALUE
        };

        int[] rowHeights = new int[numTasks];
        double[] rowWeights = new double[numTasks];

        for (int i = 0; i < numTasks; i++) {
            rowHeights[i] = 0;
            rowWeights[i] = 0.0;
        }

        panelLayout.rowHeights = rowHeights;
        panelLayout.rowWeights = rowWeights;

        elementPanel.setLayout(panelLayout);

        for (int i = 0; i < numTasks; i++) {

            JLabel nextLabel = new JLabel("");
            GridBagConstraints labelConstraints = new GridBagConstraints();
            labelConstraints.insets = elementInsets;
            labelConstraints.gridx = 0;
            labelConstraints.gridy = i;
            labelConstraints.anchor = GridBagConstraints.WEST;
            elementPanel.add(nextLabel, labelConstraints);
            taskNames.add(nextLabel);

            JProgressBar elementBar = new JProgressBar();
            elementBar.setStringPainted(true);
            elementBar.setForeground(progressBarForeground);

            elementBar.setMinimum(MIN_BAR_VALUE);
            elementBar.setValue(MIN_BAR_VALUE);
            elementBar.setMaximum(MAX_BAR_VALUE);

            GridBagConstraints barConstraints = new GridBagConstraints();
            barConstraints.insets = elementInsets;
            barConstraints.fill = GridBagConstraints.HORIZONTAL;
            barConstraints.gridwidth = 2;
            barConstraints.gridx = 1;
            barConstraints.gridy = i;
            elementPanel.add(elementBar, barConstraints);
            taskProgressBars.add(elementBar);
        }
    }
}
