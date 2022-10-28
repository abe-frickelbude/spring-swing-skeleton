package de.fb.jvips_playground.service;

import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

@Data
@Accessors(fluent = true)
public class ImageProcessingParams {

    private boolean applyOverlay;
    private boolean applyCrop;
    private boolean applyText;
    private Rectangle2D overlayBounds;
    private String text;
    private Point2D textOrigin;
    private Rectangle2D cropBounds;
    private Dimension2D outputDimension;
}
