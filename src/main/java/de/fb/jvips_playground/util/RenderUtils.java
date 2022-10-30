package de.fb.jvips_playground.util;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.kordamp.ikonli.swing.FontIcon;

public final class RenderUtils {

    public static final BasicStroke FAINT_LINE_STROKE;
    public static final BasicStroke THICK_LINE_STROKE;
    public static final Font BOLD_SYSTEM_FONT;

    private RenderUtils() {

    }

    static {

        FAINT_LINE_STROKE = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER);

        THICK_LINE_STROKE = new BasicStroke(3.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER);

        BOLD_SYSTEM_FONT = Font.decode(null).deriveFont(Font.BOLD, 14);
    }

    public static Image renderFontIcon(final FontIcon icon) {

        Image target = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, target.getGraphics(), 0, 0);
        return target;
    }
}
