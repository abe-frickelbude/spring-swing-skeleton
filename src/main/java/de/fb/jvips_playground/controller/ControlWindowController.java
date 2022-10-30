package de.fb.jvips_playground.controller;

import de.fb.jvips_playground.service.ImageProcessingParams;
import de.fb.jvips_playground.service.JVipsService;
import de.fb.jvips_playground.view.ControlWindow;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;

@Singleton
public class ControlWindowController {

    private static final Logger log = LoggerFactory.getLogger(MainWindowController.class);

    private final JVipsService jVipsService;
    private final MainWindowController mainWindowController;
    private ControlWindow controlWindow; // feedback channel

    @Inject
    public ControlWindowController(final JVipsService jVipsService,
                                   final MainWindowController mainWindowController) {
        this.jVipsService = jVipsService;
        this.mainWindowController = mainWindowController;
    }

    public void setWindow(final ControlWindow window) {
        this.controlWindow = window;
    }

    public boolean onOpenSourceFile(final File sourceFile) {
        var filePath = sourceFile.getPath();
        var fileLoadOk = jVipsService.loadSourceFile(filePath);
        if (fileLoadOk) {
            mainWindowController.setReferenceImage(jVipsService.getCurrentReferenceImage());
        }
        return fileLoadOk;
    }

    public boolean onOpenOverlaySourceFile(final File sourceFile) {
        var filePath = sourceFile.getPath();
        var fileLoadOk = jVipsService.loadOverlaySourceFile(filePath);
        if(fileLoadOk) {
            var image = jVipsService.getCurrentOverlayReferenceImage();
            mainWindowController.setOverlayReferenceImage(image);
            controlWindow.setOverlayDimension(new Dimension(image.getWidth(), image.getHeight()));
        }
        return fileLoadOk;
    }

    public void onUpdateProcessParameters(final ImageProcessingParams params) {
        mainWindowController.setProcessParameters(params);
    }

    public void onApplyTransform(final ImageProcessingParams params) {
        jVipsService.processImage(params);
    }

    public void onSaveToFile(final File targetFile) {
        jVipsService.saveToFile(targetFile);
    }
}
