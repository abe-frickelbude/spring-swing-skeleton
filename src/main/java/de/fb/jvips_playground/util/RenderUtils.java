package de.fb.jvips_playground.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import org.kordamp.ikonli.swing.FontIcon;

public final class RenderUtils {

    private RenderUtils() {

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
