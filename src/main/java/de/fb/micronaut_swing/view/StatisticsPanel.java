package de.fb.micronaut_swing.view;

import java.awt.Color;
import java.awt.Dimension;
import java.time.Duration;
import java.time.OffsetDateTime;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;

/**
 * Database statistics panel comprised of a number of fields with fixed names and order.
 *
 * @author ibragim
 *
 */
public final class StatisticsPanel extends JPanel {

    // cell locations within the table models
    private static final int STATS1_POSITION = 0;
    private static final int STATS2_POSITION = 1;
    private static final int STATS3_POSITION = 2;
    private static final int STATS4_POSITION = 3;
    private static final int STATS5_POSITION = 4;
    private static final int STATS6_POSITION = 5;
    private static final int STATS7_POSITION = 6;

    private Color gridColor;
    private int rowHeight;
    private Dimension intercellSpacing;

    private JTable statsTable;
    private DefaultTableModel tableModel;

    public StatisticsPanel() {
        super();
        // defaults
        gridColor = Color.LIGHT_GRAY;
        setRowHeight(20);
        setIntercellSpacing(new Dimension(10, 5));
        initStatsTableModel();
        initUI();
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(final Color gridColor) {
        this.gridColor = gridColor;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(final int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public Dimension getIntercellSpacing() {
        return intercellSpacing;
    }

    public void setIntercellSpacing(final Dimension intercellSpacing) {
        this.intercellSpacing = intercellSpacing;
    }

    public void setStats1(final int value) {
        tableModel.setValueAt(Integer.valueOf(value), STATS1_POSITION, 1);
    }

    public void setStats2(final int value) {
        tableModel.setValueAt(Integer.valueOf(value), STATS2_POSITION, 1);
    }

    public void setStats3(final int value) {
        tableModel.setValueAt(Integer.valueOf(value), STATS3_POSITION, 1);
    }

    public void setStats4(final int value) {
        tableModel.setValueAt(Integer.valueOf(value), STATS4_POSITION, 1);
    }

    public void setStats5(final Duration value) {
        String formattedTime = "TODO";
        tableModel.setValueAt(formattedTime, STATS5_POSITION, 1);
    }

    public void setStats6(final Duration value) {
        String formattedTime = "TODO";
        tableModel.setValueAt(formattedTime, STATS6_POSITION, 1);
    }

    public void setStats7(final OffsetDateTime value) {
        String formattedTime = "TODO";
        tableModel.setValueAt(value.toString(), STATS7_POSITION, 1);
    }

    private void initUI() {

        statsTable = new JTable();

        // since our table is read-only, we disable all editing / selection
        statsTable.setRequestFocusEnabled(false);
        statsTable.setRowSelectionAllowed(false);
        statsTable.setIntercellSpacing(intercellSpacing);
        statsTable.setGridColor(gridColor);
        statsTable.setColumnSelectionAllowed(false);
        statsTable.setCellSelectionEnabled(false);
        statsTable.setRowHeight(getRowHeight());

        statsTable.setModel(tableModel);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(statsTable);
    }

    private void initStatsTableModel() {

        tableModel = new DefaultTableModel();

        // columns
        tableModel.addColumn(StringUtils.EMPTY);
        tableModel.addColumn(StringUtils.EMPTY);

        tableModel.addRow(new Object[] {
            "stats 1", StringUtils.EMPTY
        });

        tableModel.addRow(new Object[] {
            "stats 2", StringUtils.EMPTY
        });

        tableModel.addRow(new Object[] {
            "stats 3 ", StringUtils.EMPTY
        });

        tableModel.addRow(new Object[] {
            "stats 4", StringUtils.EMPTY
        });

        tableModel.addRow(new Object[] {
            "stats 5", StringUtils.EMPTY
        });

        tableModel.addRow(new Object[] {
            "stats 6", StringUtils.EMPTY
        });

        tableModel.addRow(new Object[] {
            "stats 7", StringUtils.EMPTY
        });
    }
}
