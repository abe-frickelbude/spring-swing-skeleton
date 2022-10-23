package de.fb.micronaut_swing.view;

import de.fb.micronaut_swing.util.Colors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class JSimpleDisplayPanel extends JComponent implements
    ComponentListener,
    MouseListener,
    MouseMotionListener,
    MouseWheelListener {

    private static final Logger log = LoggerFactory.getLogger(JSimpleDisplayPanel.class);

    // mouse coordinates, buttons & modifiers
    private Point mousePosition;
    private int mouseButton;
    private int mouseModifiers;

    private Rectangle innerBounds; // adjusted for border etc

    // ----------------------- UI elements & flags ----------------------------

    private boolean crossHairEnabled;

    private JMenu viewOptionsMenu;

    private JCheckBoxMenuItem uiCrossItem;

    private BasicStroke dashStroke;
    private Color backgroundColor;
    private Color crossHairColor;
    private Color foregroundColor;

    private DecimalFormat numberFormatter; // number formatter

    public JSimpleDisplayPanel() {

        super();
        this.setDoubleBuffered(true);

        mousePosition = new Point();
        NumberFormat ff = NumberFormat.getInstance();
        numberFormatter = (DecimalFormat) ff;
        numberFormatter.setMinimumFractionDigits(4);
        numberFormatter.setMaximumFractionDigits(4);

        innerBounds = new Rectangle();

        // setup rendering attributes for visual aids
        backgroundColor = Colors.TRANSPARENT;
        foregroundColor = Color.black;
        crossHairColor = Color.black;
        crossHairEnabled = false;

        float dash[] = {2.0f};
        dashStroke = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            1.0f, dash, 0.0f);

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addComponentListener(this);
        createUI();
    }

    /**
     * Returns the menu that controls the appearance of the
     * display panel. <br>Can be inserted into a {@link JMenuBar}
     * in the parent window.
     */
    public JMenu getMenus() {
        return viewOptionsMenu;
    }

    public void setCrossHairColor(Color c) {
        crossHairColor = c;
    }

    public void setCrossHairEnabled(boolean on) {
        crossHairEnabled = on;
        uiCrossItem.setSelected(on); // sync UI
        if ((this.getHeight() != 0) && (this.getWidth() != 0))
            repaint();
    }

    public void setBackgroundColor(Color c) {
        backgroundColor = c;
        repaint();
    }

    public void setForegroundColor(Color c) {
        foregroundColor = c;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getWidth() != 0 && getHeight() != 0) {
            render(g);
        }
    }

    @Override
    public void componentHidden(ComponentEvent event) {
    }

    @Override
    public void componentMoved(ComponentEvent event) {
        SwingUtilities.calculateInnerArea(this, innerBounds);
    }

    @Override
    public void componentShown(ComponentEvent event) {
        SwingUtilities.calculateInnerArea(this, innerBounds);
        repaint();
    }

    @Override
    public void componentResized(ComponentEvent event) {
        SwingUtilities.calculateInnerArea(this, innerBounds);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
        //if (innerBounds.contains(mousePosition)) {
            mousePosition = event.getPoint();
            mouseButton = event.getButton();
            mouseModifiers = event.getModifiersEx();
            repaint();
        //}
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        //if (innerBounds.contains(mousePosition)) {
            mousePosition.setLocation(event.getPoint());
            repaint();
        //}
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        // remember current cursor position
        //if (innerBounds.contains(mousePosition)) {
            mousePosition.setLocation(event.getPoint());
            repaint();
        //}
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        repaint();
    }

    private void render(final Graphics g) {

        final var ctx = (Graphics2D) g;

        //clear everything to background color
        ctx.setBackground(backgroundColor);
        ctx.clearRect(innerBounds.x, innerBounds.y, innerBounds.width, innerBounds.height);

        //TODO: draw supplied image

        // draw cross-hair
        if (crossHairEnabled)
            drawCrossHair(ctx);

        // draw mouse coordinate information panel
        drawMouseStatus(ctx);
    }

    // create UI elements and set up various UI properties
    private void createUI() {
        createMenu();
    }

    private void createMenu() {
        viewOptionsMenu = new JMenu("View");
        // crosshair checkbox
        uiCrossItem = new JCheckBoxMenuItem("Show crosshair", false);
        uiCrossItem.setToolTipText("Show / hide crosshair");
        uiCrossItem.setAccelerator(KeyStroke.getKeyStroke('c'));
        viewOptionsMenu.add(uiCrossItem);
    }

    private void drawMouseStatus(final Graphics2D g) {
        // draw mouse coordinates on screen
        String xText = "X :  " + numberFormatter.format(mousePosition.x);
        String yText = "Y :  " + numberFormatter.format(mousePosition.y);

        FontMetrics ff = g.getFontMetrics();

        Rectangle2D xtextBounds = ff.getStringBounds(xText, g);
        Rectangle2D ytextBounds = ff.getStringBounds(yText, g);

        int font_size = g.getFont().getSize();

        int width = (int) Math.max(xtextBounds.getWidth(), ytextBounds.getWidth());

        // keep right text margin at least 10 pixels
        // away from the display border
        int x = innerBounds.width - width - 10;

        // set y to two text lines' height
        int y = innerBounds.height - 2 * font_size - 10;

        // make box for text
        // g.setColor(backgroundColor);
        // g.fillRect(x - 5, y - font_size, this.getWidth() - x + 10, this.getHeight() - y + font_size);

        g.setColor(foregroundColor);
        g.drawString(xText, x, y);
        g.drawString(yText, x, y + (int) (1.4 * font_size));
    }

    private void drawCrossHair(final Graphics2D g) {

        g.setColor(crossHairColor);
        BasicStroke lastStroke = (BasicStroke) g.getStroke();
        g.setStroke(dashStroke);

        /*
         * Since we don't care about the crosshair lines being
         * overlapped by a potential border, we just draw them using
         * component width and height.
         */

        g.drawLine(innerBounds.x, mousePosition.y,
            innerBounds.x + innerBounds.width, mousePosition.y);

        g.drawLine(mousePosition.x, innerBounds.y,
            mousePosition.x, innerBounds.y + innerBounds.height);

        g.setStroke(lastStroke);
    }
}

