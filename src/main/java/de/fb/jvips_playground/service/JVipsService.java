package de.fb.jvips_playground.service;

import com.criteo.vips.VipsContext;
import com.criteo.vips.VipsException;
import com.criteo.vips.VipsImage;
import com.criteo.vips.enums.VipsBlendMode;
import com.criteo.vips.enums.VipsImageFormat;
import com.criteo.vips.options.Composite2Options;
import com.criteo.vips.options.ResizeOptions;
import de.fb.jvips_playground.util.LogUtils;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@NotThreadSafe
@Singleton
public class JVipsService {

    private static final Logger log = LoggerFactory.getLogger(JVipsService.class);

    private final AtomicReference<VipsImage> currentImage;
    private final AtomicReference<VipsImage> currentOverlayImage;

    public JVipsService() {

        VipsContext.setLeak(true);
        this.currentImage = new AtomicReference<>();
        this.currentOverlayImage = new AtomicReference<>();
    }

    public BufferedImage getCurrentReferenceImage() {

        // as it is currently impossible to simply "get at" the image data of a VipsImage,
        // so just dump to PNG (to avoid additional lossy compression) and use that with
        // ImageIO.read() to read it back - slow and silly, but this is just for local preview purposes anyway
        BufferedImage refImage = null;
        if (currentImage.get() != null) {
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

    public void processImage(final ImageProcessingParams params) {

        if (currentImage.get() != null) {
            try {

                var sourceImage = currentImage.get();

                // 1) apply crop
                if (params.applyCrop()) {
                    log.info("Cropping input image to {}", LogUtils.formatRectangle(params.cropBounds()));
                    sourceImage.applyCrop(params.cropBounds());
                    log.info("Image cropped!");
                }

                // 2) apply overlay
                if (params.applyOverlay() && currentOverlayImage.get() != null) {

                    int x0 = params.overlayBounds().x;
                    int y0 = params.overlayBounds().y;
                    log.info("Applying overlay image at ({},{})", x0, y0);

                    sourceImage.applyComposite2(
                        currentOverlayImage.get(),
                        VipsBlendMode.Over,
                        new Composite2Options().x(x0).y(y0));

                    log.info("Overlay image rendered!");
                }

                // 3) render text
                if (params.applyText()) {
                    //TODO - JVIps can't render text yet
                }

                // 4) scale to target dimensions
                var outputSize = params.outputSize();
                if(outputSize.width != 0 && outputSize.height != 0) {
                    // using default ResizeOptions -> can be fine-tuned
                    sourceImage.applyResize(0.5);
                }

            } catch (VipsException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    public BufferedImage getCurrentOverlayReferenceImage() {
        BufferedImage refImage = null;
        if (currentOverlayImage.get() != null) {
            try {
                var imageData = currentOverlayImage.get().writeToArray(VipsImageFormat.PNG, false);
                try (var inputStream = new ByteArrayInputStream(imageData)) {
                    refImage = ImageIO.read(inputStream);
                }
            } catch (IOException | VipsException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return refImage;
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

    public void saveToFile(final File targetFile) {

        if (currentImage.get() != null) {

            log.info("Saving {}", targetFile);
            var path = targetFile.getPath();
            var extension = FilenameUtils.getExtension(targetFile.getName()).toLowerCase();
            var image = currentImage.get();
            try {

                switch (extension) {

                    case "jpg": {
                        image.writeJPEGToFile(path, 50, false);
                    }
                    break;

                    case "png": {
                        image.writePNGToFile(path, 4, false,
                            (int) Math.pow(2, 24), false);
                    }
                    break;

                    case "webp": {
                        byte[] imageData = image.writeWEBPToArray(30, false, false);
                        FileUtils.writeByteArrayToFile(targetFile, imageData);
                    }
                    break;

                    case "avif": {
                        byte[] imageData = image.writeAVIFToArray(30, false, 7);
                        FileUtils.writeByteArrayToFile(targetFile, imageData);
                    }
                    break;

                    default:
                        log.error("Unsupported format [{}]", extension);
                        break;
                }

            } catch (VipsException | IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
}
