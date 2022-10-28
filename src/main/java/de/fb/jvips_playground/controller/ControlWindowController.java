package de.fb.jvips_playground.controller;

import de.fb.jvips_playground.service.ImageProcessingParams;
import de.fb.jvips_playground.service.JVipsService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Singleton
public class ControlWindowController {

    private static final Logger logger = LoggerFactory.getLogger(MainWindowController.class);

    private final JVipsService jVipsService;
    private final MainWindowController mainWindowController;

    @Inject
    public ControlWindowController(final JVipsService jVipsService,
                                   final MainWindowController mainWindowController) {
        this.jVipsService = jVipsService;
        this.mainWindowController = mainWindowController;
    }

    public boolean onOpenSourceFile(final File sourceFile) {
        var filePath = sourceFile.getPath();
        var fileLoadOk = jVipsService.loadSourceFile(filePath);
        if(fileLoadOk) {
            mainWindowController.setReferenceImage(jVipsService.getCurrentReferenceImage());
        }
        return fileLoadOk;
    }

    public boolean onOpenOverlaySourceFile(final File sourceFile) {
        var filePath = sourceFile.getPath();
        return jVipsService.loadOverlaySourceFile(filePath);
    }

    public void onApplyTransform(final ImageProcessingParams params) {

    }

    public void onSaveToFile() {

    }
}
