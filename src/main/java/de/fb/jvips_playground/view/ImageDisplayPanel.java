package de.fb.jvips_playground.view;

import de.fb.jvips_playground.util.Colors;
import de.fb.jvips_playground.util.RenderUtils;
import de.fb.jvips_playground.view.hud.VisualAid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageDisplayPanel extends JPanel implements
    ComponentListener,
    MouseListener,
    MouseMotionListener,
    MouseWheelListener {

    private static final Logger log = LoggerFactory.getLogger(ImageDisplayPanel.class);

    // <editor-fold desc="component state">
    // mouse coordinates, buttons & modifiers
    private Point mousePosition;
    private int mouseButton;
    private int mouseModifiers;

    private Rectangle innerBounds; // adjusted for border etc
    private AffineTransform viewportTransform;

    private BufferedImage mainImage;

    // ----------------------- UI elements & flags ----------------------------

    private boolean crossHairEnabled;
    private boolean antiAliasingEnabled;

    private Color backgroundColor;
    private Color foregroundColor;
    private Color crossHairColor;

    private final BasicStroke crossHairStroke;
    private final DecimalFormat numberFormatter;
    private java.util.List<VisualAid> visualAids;

    // </editor-fold>

    public ImageDisplayPanel() {

        super();
        this.setDoubleBuffered(true);
        this.setAutoscrolls(true);

        mousePosition = new Point();
        NumberFormat ff = NumberFormat.getInstance();
        numberFormatter = (DecimalFormat) ff;
        numberFormatter.setMinimumFractionDigits(4);
        numberFormatter.setMaximumFractionDigits(4);

        innerBounds = new Rectangle();
        viewportTransform = new AffineTransform();

        visualAids = new ArrayList<>();

        // setup rendering attributes for visual aids
        backgroundColor = Colors.TRANSPARENT;
        foregroundColor = Color.black;
        crossHairColor = Color.black;
        crossHairEnabled = false;
        antiAliasingEnabled = false;

        float dash[] = {2.0f};
        crossHairStroke = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            1.0f, dash, 0.0f);

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addComponentListener(this);
    }

    public void setMainImage(final BufferedImage image) {
        this.mainImage = image;
        // update component size for proper scrolling
        this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        revalidate();
    }

    public void setCrossHairColor(Color c) {
        crossHairColor = c;
    }

    public void setCrossHairEnabled(boolean enabled) {
        crossHairEnabled = enabled;
        if ((this.getHeight() != 0) && (this.getWidth() != 0))
            repaint();
    }

    public void setAntiAliasingEnabled(boolean enabled) {
        antiAliasingEnabled = enabled;
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

    public void addVisualAid(final VisualAid aid) {
        visualAids.add(aid);
    }

    public void clearVisualAids() {
        visualAids.clear();
    }

    public void setVisualAids(final List<VisualAid> aids) {
        this.visualAids = aids;
    }

    /**
     * WARNING: this does not respect the "offset" coordinate system (i.e. minus borders/insets)
     * and will happily paint under/over them! The whole component should be wrapped e.g. in another
     * JPanel container and then any borders etc set on this container, rather than the component
     * itself!
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getWidth() != 0 && getHeight() != 0) {
            render((Graphics2D) g);
        }
    }

    // <editor-fold desc="event handlers">
    @Override
    public void componentHidden(ComponentEvent event) {
    }

    @Override
    public void componentMoved(ComponentEvent event) {
//        SwingUtilities.calculateInnerArea(this, innerBounds);
//        calculateViewportTransform();
    }

    @Override
    public void componentShown(ComponentEvent event) {
//        SwingUtilities.calculateInnerArea(this, innerBounds);
//        calculateViewportTransform();
        repaint();
    }

    @Override
    public void componentResized(ComponentEvent event) {
//        SwingUtilities.calculateInnerArea(this, innerBounds);
//        calculateViewportTransform();
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
        mousePosition = event.getPoint();
        mouseButton = event.getButton();
        mouseModifiers = event.getModifiersEx();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        mousePosition.setLocation(event.getPoint());
        //if(event.getButton() == MouseEvent.BUTTON2) {
        // right-clicked -> scroll
        // works but is kinda glitchy, disabled for now
        // var rect = new Rectangle(event.getX(), event.getY(), 1,1);
        // scrollRectToVisible(rect);
        // log.info("Scrolling to ({},{})", event.getX(), event.getY());
        //}
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        mousePosition.setLocation(event.getPoint());
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        repaint();
    }
    // </editor-fold>

    private void calculateViewportTransform() {
        viewportTransform.setToTranslation(innerBounds.getX(), innerBounds.getY());
    }

    private void render(final Graphics2D ctx) {

        ctx.setBackground(backgroundColor);
        ctx.clearRect(0, 0, getWidth(), getHeight());

        if (mainImage != null) {
            ctx.drawImage(mainImage, 0, 0, null);
        }

        // temporarily turn on antialiasing for visual aids
        if (antiAliasingEnabled) {
            ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        drawVisualAids(ctx);
        if (crossHairEnabled) {
            drawCrossHair(ctx);
        }
        drawMouseStatus(ctx);

        if (antiAliasingEnabled) {
            ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }

    private void drawVisualAids(final Graphics2D ctx) {
        for (var element : visualAids) {
            element.draw(ctx);
        }
    }

    private void drawMouseStatus(final Graphics2D ctx) {

        var prevFont = ctx.getFont();
        ctx.setFont(RenderUtils.BOLD_SYSTEM_FONT);

        // draw mouse coordinates on screen
        var xText = "X:  " + mousePosition.x;
        var yText = "Y:  " + mousePosition.y;
        var ff = ctx.getFontMetrics();
        var xtextBounds = ff.getStringBounds(xText, ctx);
        var ytextBounds = ff.getStringBounds(yText, ctx);

        int font_size = ctx.getFont().getSize();

        int width = (int) Math.max(xtextBounds.getWidth(), ytextBounds.getWidth());

        // here I cheat a little to always keep the mouse status within the visible rectangle
        var visibleRect = this.getVisibleRect();

        // keep right text margin at least a few pixels away from the display border
        int x = visibleRect.width + visibleRect.x - width - 5;

        // set y to two text lines' height
        int y = visibleRect.height + visibleRect.y - 2 * font_size - 5;

        ctx.setColor(foregroundColor);
        ctx.drawString(xText, x, y);
        ctx.drawString(yText, x, y + (int) (1.4 * font_size));

        ctx.setFont(prevFont);
    }

    private void drawCrossHair(final Graphics2D ctx) {

        ctx.setColor(crossHairColor);
        var lastStroke = ctx.getStroke();
        ctx.setStroke(crossHairStroke);
        ctx.drawLine(0, mousePosition.y, getWidth(), mousePosition.y);
        ctx.drawLine(mousePosition.x, 0, mousePosition.x, getHeight());
        ctx.setStroke(lastStroke);
    }
}

