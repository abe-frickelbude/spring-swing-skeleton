package de.fb.jvips_playground.util;

import java.awt.*;

public final class LogUtils {

    public static String formatRectangle(final Rectangle rectangle) {
        return String.format("[x: %d, y: %d, w: %d, h: %d]", rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
