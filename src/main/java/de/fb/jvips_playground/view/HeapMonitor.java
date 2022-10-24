package de.fb.jvips_playground.view;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeapMonitor extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(HeapMonitor.class);

    // in milliseconds
    private static final int DEFAULT_UPDATE_INTERVAL = 1000;

    // number of bytes in one MB
    private static final int ONE_MEGABYTE = 1048576;

    private static final Font BAR_FONT = new Font("Tahoma", Font.PLAIN, 11);

    // usage bar info string pattern
    private static final String INFO_BAR_PATTERN = "%4d MB / %4d MB";

    private final MemoryMXBean memoryMxBean;
    private JProgressBar heapUsageBar;
    private JProgressBar nonHeapUsageBar;

    private final Thread updateThread;
    private final AtomicBoolean updateIsActive;
    private final AtomicInteger updateInterval;

    /**
     * Create the panel.
     */
    public HeapMonitor() {

        memoryMxBean = ManagementFactory.getMemoryMXBean();
        updateThread = new Thread(this::periodicUpdate, "heap monitor update hread");
        updateIsActive = new AtomicBoolean(false);
        updateInterval = new AtomicInteger(DEFAULT_UPDATE_INTERVAL);
        initializeUI();
    }

    /**
     * Manual update call
     */
    public void updateStatistics() {
        SwingUtilities.invokeLater(this::updateHeapStatistics);
    }

    @Override
    public void setEnabled(final boolean enabled) {

        super.setEnabled(enabled);
        updateIsActive.set(enabled);

        try {
            if (!updateThread.isAlive()) {
                updateThread.start();
            }

        } catch (IllegalThreadStateException ex) {
            logger.error(ex.getMessage());
        }
    }

    public void setUpdateInterval(final int updateInterval) {
        if (updateInterval > 0) {
            this.updateInterval.set(updateInterval);
        }
    }

    private void initializeUI() {

        setBorder(new TitledBorder(null, "Heap usage monitor",
            TitledBorder.LEFT, TitledBorder.TOP, null, null));

        heapUsageBar = new JProgressBar();
        heapUsageBar.setStringPainted(true);
        heapUsageBar.setPreferredSize(new Dimension(100, 28));
        heapUsageBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        heapUsageBar.setFont(BAR_FONT);
        heapUsageBar.setMinimum(0);
        heapUsageBar.setMaximum(1);
        heapUsageBar.setString("");
        heapUsageBar.setValue(0);

        nonHeapUsageBar = new JProgressBar();
        nonHeapUsageBar.setFont(BAR_FONT);
        nonHeapUsageBar.setPreferredSize(new Dimension(100, 28));
        nonHeapUsageBar.setStringPainted(true);
        nonHeapUsageBar.setString("");
        nonHeapUsageBar.setMinimum(0);
        nonHeapUsageBar.setMaximum(1);
        // nonHeapUsageBar.setForeground(new Color(0, 0, 128));

        JLabel heapBarLabel = new JLabel("Heap (used/max)");
        JLabel nonHeapLabel = new JLabel("Non-Heap (used/max)");

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(heapBarLabel)
                        .addComponent(heapUsageBar, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(nonHeapLabel)
                            .addGap(150))
                        .addGroup(groupLayout.createSequentialGroup()
                            .addComponent(nonHeapUsageBar, GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .addContainerGap()))));
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(nonHeapLabel)
                        .addComponent(heapBarLabel))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                        .addComponent(heapUsageBar, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                        .addComponent(nonHeapUsageBar, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                    .addGap(59)));
        setLayout(groupLayout);
    }

    private void periodicUpdate() {

        // run periodic updates
        while (true) {
            if (updateIsActive.get() == true) {

                SwingUtilities.invokeLater(() -> {
                    updateHeapStatistics();
                });

                try {
                    Thread.sleep(updateInterval.get());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Update heap usage bar with current memory statistics.
     */
    private void updateHeapStatistics() {

        MemoryUsage usage = memoryMxBean.getHeapMemoryUsage();

        heapUsageBar.setMaximum(byteToMB(usage.getMax()));
        heapUsageBar.setValue(byteToMB(usage.getUsed()));
        heapUsageBar.setString(formatInfoString(byteToMB(usage.getUsed()), byteToMB(usage.getMax())));
        heapUsageBar.repaint();

        usage = memoryMxBean.getNonHeapMemoryUsage();
        nonHeapUsageBar.setMaximum(byteToMB(usage.getMax()));
        nonHeapUsageBar.setValue(byteToMB(usage.getUsed()));
        nonHeapUsageBar.setString(formatInfoString(byteToMB(usage.getUsed()), byteToMB(usage.getMax())));
        nonHeapUsageBar.repaint();
    }

    private int byteToMB(final long bytes) {
        return (int) (bytes / ONE_MEGABYTE);
    }

    private String formatInfoString(final int usedHeap, final int maxHeap) {
        return String.format(INFO_BAR_PATTERN, usedHeap, maxHeap);
    }
}
