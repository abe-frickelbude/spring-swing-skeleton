package de.fb.jvips_playground.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import org.kordamp.ikonli.swing.FontIcon;

public final class RenderUtils {

    public static final BasicStroke FAINT_LINE_STROKE;

    private RenderUtils() {

    }

    static {
        FAINT_LINE_STROKE = new BasicStroke(0.5f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER);
    }

    /**
     * Adapter method to render a FontIcon into an Image
     *
     * @param icon
     * @return
     */
    public static Image renderFontIcon(final FontIcon icon) {

        Image target = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, target.getGraphics(), 0, 0);
        return target;
    }
}
