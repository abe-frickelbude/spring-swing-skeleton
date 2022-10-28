package de.fb.jvips_playground.service;

import com.criteo.vips.VipsException;
import com.criteo.vips.VipsImage;
import com.criteo.vips.enums.VipsImageFormat;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@NotThreadSafe
@Singleton
public class JVipsService {

    private static final Logger log = LoggerFactory.getLogger(JVipsService.class);

    private final AtomicReference<VipsImage> currentImage;
    private final AtomicReference<VipsImage> currentOverlayImage;

    public JVipsService() {
        this.currentImage = new AtomicReference<>();
        this.currentOverlayImage = new AtomicReference<>();
    }

    public BufferedImage getCurrentReferenceImage() {

        BufferedImage refImage = null;
        if (currentImage.get() != null) {
            // as it is currently impossible to simply "get at" the image data of a VipsImage,
            // so just dump to PNG (to avoid additional lossy compression) and use that with
            // ImageIO.read() to read it back - slow and silly, but this is just for local preview purposes anyway
            try {
                var imageData = currentImage.get().writeToArray(VipsImageFormat.PNG, false);
                try (var inputStream = new ByteArrayInputStream(imageData)) {
                    refImage = ImageIO.read(inputStream);
                }
            } catch (IOException | VipsException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return refImage;
    }

    public BufferedImage getCurrentOverlayReferenceImage() {
        return null;
    }

    public boolean loadSourceFile(final String filePath) {
        try {
            var image = new VipsImage(filePath);
            if (currentImage.get() != null) {
                currentImage.get().release(); // release previous image
            }
            currentImage.set(image);
            log.info("Loaded image {}, original size: {}x{}", filePath, image.getWidth(), image.getHeight());
            return true;
        } catch (VipsException ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }

    public boolean loadOverlaySourceFile(final String filePath) {
        try {
            var image = new VipsImage(filePath);
            if (currentOverlayImage.get() != null) {
                currentOverlayImage.get().release(); // release previous image
            }
            currentOverlayImage.set(image);
            log.info("Loaded overlay image {}, original size: {}x{}", filePath, image.getWidth(), image.getHeight());
            return true;
        } catch (VipsException ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }

    @PreDestroy
    public void shutdown() {
        if (currentImage.get() != null) {
            currentImage.get().release();
        }
        if (currentOverlayImage.get() != null) {
            currentOverlayImage.get().release();
        }
    }
}
