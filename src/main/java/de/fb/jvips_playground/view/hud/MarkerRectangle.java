package de.fb.jvips_playground.view.hud;

import de.fb.jvips_playground.util.Colors;
import de.fb.jvips_playground.util.RenderUtils;
import lombok.Getter;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@Getter
public class MarkerRectangle implements VisualAid {

    private final Rectangle2D rect;
    private final Color foregroundColor;
    private final Color backgroundColor;

    public MarkerRectangle(Rectangle2D rect) {
        this(rect, Color.green, Colors.TRANSPARENT);
    }

    public MarkerRectangle(Rectangle2D rect, Color foregroundColor) {
        this(rect, foregroundColor, Colors.TRANSPARENT);
    }

    public MarkerRectangle(Rectangle2D rect,
                           Color foregroundColor,
                           Color backgroundColor) {
        this.rect = new Rectangle2D.Double(rect.getX(),rect.getY(),rect.getWidth(), rect.getHeight());
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void draw(final Graphics2D ctx) {

        var prevBackground = ctx.getBackground();
        var prevForeground = ctx.getColor();
        var prevStroke = ctx.getStroke();

        ctx.setBackground(backgroundColor);
        ctx.setColor(foregroundColor);
        ctx.setStroke(RenderUtils.THICK_LINE_STROKE);
        ctx.draw(rect);
        ctx.setBackground(prevBackground);
        ctx.setColor(prevForeground);
        ctx.setStroke(prevStroke);
    }
}
