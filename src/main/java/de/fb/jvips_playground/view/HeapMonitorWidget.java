package de.fb.jvips_playground.view;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeapMonitorWidget extends JPanel implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(HeapMonitorWidget.class);

    // update interval, in seconds
    private static final int UPDATE_INTERVAL = 1;

    // usage bar info string pattern
    private static final String INFO_BAR_PATTERN = "%4d MB / %4d MB";

    // number of bytes in one MB
    private static final int ONE_MEGABYTE = 1048576;

    // progress bar font
    private static final Font BAR_FONT = new Font("Tahoma", Font.PLAIN, 11);

    private MemoryMXBean memoryMxBean;
    private final JProgressBar heapUsageBar;
    private final JProgressBar nonHeapUsageBar;

    private Thread updateThread;
    private AtomicBoolean updateIsActive;

    /**
     * Create the panel.
     */
    public HeapMonitorWidget() {

        setBorder(new TitledBorder(null, "Heap usage monitor", TitledBorder.LEFT, TitledBorder.TOP, null, null));

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
                        .addContainerGap())))
            );
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
                .addGap(59))
            );
        setLayout(groupLayout);
        init();
    }

    @Override
    public void run() {

        // run periodic updates
        while (true) {

            if (updateIsActive.get() == true) {
                try {
                    updateHeapStatistics();
                    Thread.sleep(TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL));

                } catch (InterruptedException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * Manual update call
     */
    public void update() {
        updateHeapStatistics();
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

    /**
     * Non-Swing initialization code
     */
    private void init() {
        memoryMxBean = ManagementFactory.getMemoryMXBean();
        updateThread = new Thread(this, "heap monitor update hread");
        updateIsActive = new AtomicBoolean(false);
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

    /**
     * Convert byte values to megabyte units
     *
     * @param bytes
     * @return
     */
    private int byteToMB(final long bytes) {
        return (int) (bytes / ONE_MEGABYTE);
    }

    /**
     * Format the information string for the usage bar using the supplied
     * values.
     *
     * @param usedHeap
     * @param maxHeap
     * @return
     */
    private String formatInfoString(final int usedHeap, final int maxHeap) {
        return String.format(INFO_BAR_PATTERN, Integer.valueOf(usedHeap), Integer.valueOf(maxHeap));
    }
}
