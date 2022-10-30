package de.fb.jvips_playground.service;

import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;
import java.awt.geom.Point2D;

@Data
@Accessors(fluent = true)
public class ImageProcessingParams {

    private boolean applyOverlay;
    private boolean applyCrop;
    private boolean applyText;
    private Rectangle overlayBounds;
    private String text;
    private Point2D textOrigin;
    private Rectangle cropBounds;
    private Dimension outputSize;
}
