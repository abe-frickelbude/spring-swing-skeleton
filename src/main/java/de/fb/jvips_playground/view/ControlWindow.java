package de.fb.jvips_playground.view;

import de.fb.jvips_playground.annotations.SwingView;
import de.fb.jvips_playground.controller.ControlWindowController;
import de.fb.jvips_playground.service.ImageProcessingParams;
import de.fb.jvips_playground.view.controls.VipsControlPanel;
import jakarta.inject.Inject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SwingView
public class ControlWindow extends JFrame {

    private final VipsControlPanel controlPanel;
    private final List<JComponent> toggleableControls;
    private final ControlWindowController controller;

    private File currentDirectory;

    @Inject
    public ControlWindow(final ControlWindowController controller) {
        this.controller = controller;
        controlPanel = new VipsControlPanel();
        toggleableControls = new ArrayList<>();
        initUI();
        connectEventHandlers();
    }

    // only GUI & glue code here, the control logic is  in the ControlWindowController
    // Note: all GUI event glue logic is here, the controller is not directly involved in UI state
    // manipulation, except via method return values
    private void initUI() {

        setTitle("Controls");

        // prevent this window from closing
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 692, 765);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        contentPane.add(controlPanel.$$$getRootComponent$$$(), BorderLayout.CENTER);

        toggleableControls.add(controlPanel.getOverlayImageCheckbox());
        toggleableControls.add(controlPanel.getInsetTextCheckbox());
        toggleableControls.add(controlPanel.getCropCheckbox());
        toggleableControls.add(controlPanel.getApplyTransformButton());
        toggleableControls.add(controlPanel.getSaveButton());

        setControlsEnabled(false);
    }

    public void setControlsEnabled(final boolean enabled) {
        toggleableControls.forEach(control -> control.setEnabled(enabled));
    }

    private void connectEventHandlers() {

        controlPanel.getOpenInputImageButton().addActionListener(event -> {
            var inputFile = openFileDialog();
            if (inputFile != null) {
                var fileLoadOk = controller.onOpenSourceFile(inputFile);
                if (fileLoadOk) {
                    setControlsEnabled(true);
                }
            }
        });

        controlPanel.getOpenOverlayImageButton().addActionListener(event -> {
            var inputFile = openFileDialog();
            if (inputFile != null) {
                controller.onOpenOverlaySourceFile(inputFile);
            }
        });

        controlPanel.getApplyTransformButton().addActionListener(event -> {
            controller.onApplyTransform(gatherParams());
        });
    }

    private ImageProcessingParams gatherParams() {

        var params = new ImageProcessingParams();
        params.applyOverlay(controlPanel.getOverlayImageCheckbox().isSelected());
        params.applyText(controlPanel.getInsetTextCheckbox().isSelected());
        params.applyCrop(controlPanel.getCropCheckbox().isSelected());
        params.text(controlPanel.getInsetTextField().getText());

        //params.textOrigin(new Point2D.Float(controlPanel.getInsetTextSpinnerX().get))
        return params;
    }

    private File openFileDialog() {

        final JFileChooser chooser = new JFileChooser();
        final FileNameExtensionFilter extFilter = new FileNameExtensionFilter(
            "Image files",
            "jpg", "jpeg", "png");
        chooser.setFileFilter(extFilter);

        if (currentDirectory != null) {
            chooser.setCurrentDirectory(currentDirectory);
        }

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            currentDirectory = chooser.getCurrentDirectory(); // remember last dir
            return chooser.getSelectedFile();
        }
        return null;
    }
}
