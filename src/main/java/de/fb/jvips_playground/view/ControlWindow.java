package de.fb.jvips_playground.view;

import de.fb.jvips_playground.annotations.SwingView;
import de.fb.jvips_playground.controller.ControlWindowController;
import de.fb.jvips_playground.service.ImageProcessingParams;
import de.fb.jvips_playground.view.controls.VipsControlPanel;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SwingView
public class ControlWindow extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(ControlWindow.class);

    private final VipsControlPanel controlPanel;
    private final List<JComponent> toggleableControls;
    private final List<JSpinner> spinners;

    private final ControlWindowController controller;

    private File currentDirectory;

    @Inject
    public ControlWindow(final ControlWindowController controller) {
        this.controller = controller;
        controlPanel = new VipsControlPanel();
        toggleableControls = new ArrayList<>();
        spinners = new ArrayList<>();
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

        // initially disable inputs
        controlPanel.getOpenOverlayImageButton().setEnabled(false);
        controlPanel.getOverlaySpinnerX().setEnabled(false);
        controlPanel.getOverlaySpinnerY().setEnabled(false);
        controlPanel.getOverlaySpinnerWidth().setEnabled(false);
        controlPanel.getOverlaySpinnerHeight().setEnabled(false);
        controlPanel.getInsetTextField().setEnabled(false);
        controlPanel.getInsetTextSpinnerX().setEnabled(false);
        controlPanel.getInsetTextSpinnerY().setEnabled(false);
        controlPanel.getCropSpinnerX().setEnabled(false);
        controlPanel.getCropSpinnerY().setEnabled(false);
        controlPanel.getCropSpinnerWidth().setEnabled(false);
        controlPanel.getCropSpinnerHeight().setEnabled(false);

        // conditionally enable/disable controls based on checkboxes
        controlPanel.getOverlayImageCheckbox().addItemListener(event -> {
            var enabled = event.getStateChange() == ItemEvent.SELECTED;
            controlPanel.getOpenOverlayImageButton().setEnabled(enabled);
            controlPanel.getOverlaySpinnerX().setEnabled(enabled);
            controlPanel.getOverlaySpinnerY().setEnabled(enabled);
            controlPanel.getOverlaySpinnerWidth().setEnabled(enabled);
            controlPanel.getOverlaySpinnerHeight().setEnabled(enabled);
        });

        controlPanel.getInsetTextCheckbox().addItemListener(event -> {
            var enabled = event.getStateChange() == ItemEvent.SELECTED;
            controlPanel.getInsetTextField().setEnabled(enabled);
            controlPanel.getInsetTextSpinnerX().setEnabled(enabled);
            controlPanel.getInsetTextSpinnerY().setEnabled(enabled);
        });

        controlPanel.getCropCheckbox().addItemListener(event -> {
            var enabled = event.getStateChange() == ItemEvent.SELECTED;
            controlPanel.getCropSpinnerX().setEnabled(enabled);
            controlPanel.getCropSpinnerY().setEnabled(enabled);
            controlPanel.getCropSpinnerWidth().setEnabled(enabled);
            controlPanel.getCropSpinnerHeight().setEnabled(enabled);
        });

        toggleableControls.add(controlPanel.getOverlayImageCheckbox());
        toggleableControls.add(controlPanel.getInsetTextCheckbox());
        toggleableControls.add(controlPanel.getCropCheckbox());
        toggleableControls.add(controlPanel.getApplyTransformButton());
        toggleableControls.add(controlPanel.getSaveButton());

        spinners.add(controlPanel.getOverlaySpinnerX());
        spinners.add(controlPanel.getOverlaySpinnerY());
        spinners.add(controlPanel.getOverlaySpinnerWidth());
        spinners.add(controlPanel.getOverlaySpinnerHeight());
        //inputFields.add(controlPanel.getInsetTextField());
        spinners.add(controlPanel.getInsetTextSpinnerX());
        spinners.add(controlPanel.getInsetTextSpinnerY());
        spinners.add(controlPanel.getCropSpinnerX());
        spinners.add(controlPanel.getCropSpinnerY());
        spinners.add(controlPanel.getCropSpinnerWidth());
        spinners.add(controlPanel.getCropSpinnerHeight());
        spinners.add(controlPanel.getOutputSizeSpinnerX());
        spinners.add(controlPanel.getOutputSizeSpinnerY());

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

        // this is generic - any change event on any of the spinners gathers all
        // params to simplify the overall logic
        for (var spinner : spinners) {
            spinner.addChangeListener(event -> {
               controller.onUpdateProcessParameters(gatherParams());
            });
        }
    }

    private ImageProcessingParams gatherParams() {

        var params = new ImageProcessingParams();
        params.applyOverlay(controlPanel.getOverlayImageCheckbox().isSelected());
        params.applyText(controlPanel.getInsetTextCheckbox().isSelected());
        params.applyCrop(controlPanel.getCropCheckbox().isSelected());
        params.text(controlPanel.getInsetTextField().getText());

        params.overlayBounds(new Rectangle2D.Float(
           getSpinnerValue(controlPanel.getOverlaySpinnerX()),
           getSpinnerValue(controlPanel.getOverlaySpinnerY()),
           getSpinnerValue(controlPanel.getOverlaySpinnerWidth()),
           getSpinnerValue(controlPanel.getOverlaySpinnerHeight())
        ));

        params.textOrigin(new Point2D.Float(
            getSpinnerValue(controlPanel.getInsetTextSpinnerX()),
            getSpinnerValue(controlPanel.getInsetTextSpinnerY())
        ));

        params.cropBounds(new Rectangle2D.Float(
            getSpinnerValue(controlPanel.getCropSpinnerX()),
            getSpinnerValue(controlPanel.getCropSpinnerY()),
            getSpinnerValue(controlPanel.getCropSpinnerWidth()),
            getSpinnerValue(controlPanel.getCropSpinnerHeight())
        ));

        params.outputDimension(new Dimension(
           getSpinnerValue(controlPanel.getOutputSizeSpinnerX()),
           getSpinnerValue(controlPanel.getOutputSizeSpinnerY())
        ));

        return params;
    }

    private Integer getSpinnerValue(final JSpinner spinner) {
        return (Integer) spinner.getModel().getValue();
    }

    private File openFileDialog() {

        final JFileChooser chooser = new JFileChooser();
        final FileNameExtensionFilter extFilter = new FileNameExtensionFilter(
            "Image files",
            "jpg", "jpeg", "png");
        chooser.setFileFilter(extFilter);

        var dim = new Dimension(800, 600);
        chooser.setMinimumSize(dim);
        chooser.setPreferredSize(dim);

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
