package de.fb.jvips_playground.view.controls;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

@Getter
public class VipsControlPanel {

    private JPanel controlPanel;
    private JSpinner overlaySpinnerX;
    private JSpinner overlaySpinnerY;
    private JLabel ySpinnerLabel;
    private JSpinner overlaySpinnerHeight;
    private JButton openOverlayImageButton;
    private JTextField insetTextField;
    private JSpinner insetTextSpinnerX;
    private JSpinner insetTextSpinnerY;
    private JSpinner cropSpinnerX;
    private JSpinner cropSpinnerY;
    private JSpinner cropSpinnerWidth;
    private JSpinner cropSpinnerHeight;
    private JSpinner overlaySpinnerWidth;
    private JButton openInputImageButton;
    private JCheckBox overlayImageCheckbox;
    private JCheckBox insetTextCheckbox;
    private JCheckBox cropCheckbox;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JButton applyTransformButton;
    private JButton saveButton;

    public VipsControlPanel() {
        createUIComponents();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:10dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,fill:5dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,center:8dlu:noGrow,center:8dlu:noGrow,top:14dlu:noGrow,center:27px:noGrow,center:8dlu:noGrow,center:max(d;4px):noGrow,center:8dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:6dlu:noGrow,center:6dlu:noGrow,top:14dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:8dlu:noGrow,center:8dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,center:max(d;4px):noGrow,center:6dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:8dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,top:4dlu:noGrow,center:8dlu:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,center:max(d;4px):noGrow"));
        final JLabel label1 = new JLabel();
        label1.setText("X");
        CellConstraints cc = new CellConstraints();
        controlPanel.add(label1, new CellConstraints(3, 11, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));
        overlaySpinnerX = new JSpinner();
        controlPanel.add(overlaySpinnerX, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        overlaySpinnerY = new JSpinner();
        controlPanel.add(overlaySpinnerY, cc.xy(9, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        ySpinnerLabel = new JLabel();
        ySpinnerLabel.setText("Y");
        controlPanel.add(ySpinnerLabel, cc.xy(7, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setText("Width");
        controlPanel.add(label2, new CellConstraints(3, 13, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));
        final JLabel label3 = new JLabel();
        label3.setText("Height");
        controlPanel.add(label3, cc.xy(7, 13, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        overlaySpinnerHeight = new JSpinner();
        controlPanel.add(overlaySpinnerHeight, cc.xy(9, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JSeparator separator1 = new JSeparator();
        separator1.setForeground(new Color(-7566196));
        controlPanel.add(separator1, cc.xyw(3, 15, 7, CellConstraints.FILL, CellConstraints.TOP));
        openOverlayImageButton = new JButton();
        openOverlayImageButton.setText("Open overlay image...");
        controlPanel.add(openOverlayImageButton, cc.xyw(3, 7, 7));
        insetTextField = new JTextField();
        controlPanel.add(insetTextField, cc.xyw(3, 18, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("X");
        controlPanel.add(label4, cc.xy(3, 20, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setText("Y");
        controlPanel.add(label5, cc.xy(7, 20, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        insetTextSpinnerX = new JSpinner();
        controlPanel.add(insetTextSpinnerX, cc.xy(5, 20, CellConstraints.FILL, CellConstraints.DEFAULT));
        insetTextSpinnerY = new JSpinner();
        controlPanel.add(insetTextSpinnerY, cc.xy(9, 20, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JSeparator separator2 = new JSeparator();
        separator2.setForeground(new Color(-7566196));
        controlPanel.add(separator2, cc.xyw(3, 22, 7, CellConstraints.FILL, CellConstraints.FILL));
        final JLabel label6 = new JLabel();
        label6.setRequestFocusEnabled(false);
        label6.setText("X");
        controlPanel.add(label6, cc.xy(3, 27, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label7 = new JLabel();
        label7.setText("Y");
        controlPanel.add(label7, cc.xy(7, 27, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        cropSpinnerX = new JSpinner();
        controlPanel.add(cropSpinnerX, cc.xy(5, 27, CellConstraints.FILL, CellConstraints.DEFAULT));
        cropSpinnerY = new JSpinner();
        controlPanel.add(cropSpinnerY, cc.xy(9, 27, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label8 = new JLabel();
        label8.setText("Width");
        controlPanel.add(label8, cc.xy(3, 29, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label9 = new JLabel();
        label9.setText("Height");
        controlPanel.add(label9, cc.xy(7, 29));
        cropSpinnerWidth = new JSpinner();
        controlPanel.add(cropSpinnerWidth, cc.xy(5, 29, CellConstraints.FILL, CellConstraints.DEFAULT));
        cropSpinnerHeight = new JSpinner();
        controlPanel.add(cropSpinnerHeight, cc.xy(9, 29, CellConstraints.FILL, CellConstraints.DEFAULT));
        overlaySpinnerWidth = new JSpinner();
        controlPanel.add(overlaySpinnerWidth, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        openInputImageButton = new JButton();
        openInputImageButton.setText("Open input image...");
        controlPanel.add(openInputImageButton, cc.xyw(3, 3, 7));
        final JSeparator separator3 = new JSeparator();
        separator3.setForeground(new Color(-7566196));
        controlPanel.add(separator3, cc.xyw(3, 5, 7, CellConstraints.FILL, CellConstraints.FILL));
        overlayImageCheckbox = new JCheckBox();
        overlayImageCheckbox.setText("");
        controlPanel.add(overlayImageCheckbox, cc.xy(9, 6, CellConstraints.RIGHT, CellConstraints.CENTER));
        final JLabel label10 = new JLabel();
        label10.setText("Overlay image");
        controlPanel.add(label10, cc.xy(3, 6, CellConstraints.LEFT, CellConstraints.FILL));
        final JLabel label11 = new JLabel();
        label11.setText("Text inset");
        controlPanel.add(label11, cc.xy(3, 16, CellConstraints.LEFT, CellConstraints.CENTER));
        final JLabel label12 = new JLabel();
        label12.setText("Crop rectangle");
        controlPanel.add(label12, cc.xy(3, 24, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label13 = new JLabel();
        label13.setText("Output dimensions");
        controlPanel.add(label13, cc.xy(3, 32));
        final JSeparator separator4 = new JSeparator();
        separator4.setForeground(new Color(-7566196));
        controlPanel.add(separator4, cc.xyw(3, 31, 7, CellConstraints.FILL, CellConstraints.CENTER));
        final JLabel label14 = new JLabel();
        label14.setText("Width");
        controlPanel.add(label14, cc.xy(3, 34, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        spinner1 = new JSpinner();
        controlPanel.add(spinner1, cc.xy(5, 34, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label15 = new JLabel();
        label15.setText("Height");
        controlPanel.add(label15, cc.xy(7, 34, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        spinner2 = new JSpinner();
        controlPanel.add(spinner2, cc.xy(9, 34, CellConstraints.FILL, CellConstraints.DEFAULT));
        cropCheckbox = new JCheckBox();
        cropCheckbox.setText("");
        controlPanel.add(cropCheckbox, cc.xy(9, 24, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        insetTextCheckbox = new JCheckBox();
        insetTextCheckbox.setText("");
        controlPanel.add(insetTextCheckbox, cc.xy(9, 16, CellConstraints.RIGHT, CellConstraints.CENTER));
        final JLabel label16 = new JLabel();
        label16.setText("Bounds");
        controlPanel.add(label16, cc.xy(3, 9));
        final JSeparator separator5 = new JSeparator();
        separator5.setForeground(new Color(-7566196));
        controlPanel.add(separator5, cc.xyw(3, 37, 7, CellConstraints.FILL, CellConstraints.FILL));
        applyTransformButton = new JButton();
        applyTransformButton.setText("Apply transform");
        controlPanel.add(applyTransformButton, cc.xyw(3, 40, 7));
        saveButton = new JButton();
        saveButton.setText("Save...");
        controlPanel.add(saveButton, cc.xyw(3, 42, 7));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return controlPanel;
    }

    private void createUIComponents() {

        // initially disable inputs
        openOverlayImageButton.setEnabled(false);
        overlaySpinnerX.setEnabled(false);
        overlaySpinnerY.setEnabled(false);
        overlaySpinnerWidth.setEnabled(false);
        overlaySpinnerHeight.setEnabled(false);
        insetTextField.setEnabled(false);
        insetTextSpinnerX.setEnabled(false);
        insetTextSpinnerY.setEnabled(false);
        cropSpinnerX.setEnabled(false);
        cropSpinnerY.setEnabled(false);
        cropSpinnerWidth.setEnabled(false);
        cropSpinnerHeight.setEnabled(false);

        overlayImageCheckbox.addItemListener(event -> {
            var enabled = event.getStateChange() == ItemEvent.SELECTED;
            openOverlayImageButton.setEnabled(enabled);
            overlaySpinnerX.setEnabled(enabled);
            overlaySpinnerY.setEnabled(enabled);
            overlaySpinnerWidth.setEnabled(enabled);
            overlaySpinnerHeight.setEnabled(enabled);
        });

        insetTextCheckbox.addItemListener(event -> {
            var enabled = event.getStateChange() == ItemEvent.SELECTED;
            insetTextField.setEnabled(enabled);
            insetTextSpinnerX.setEnabled(enabled);
            insetTextSpinnerY.setEnabled(enabled);
        });

        cropCheckbox.addItemListener(event -> {
            var enabled = event.getStateChange() == ItemEvent.SELECTED;
            cropSpinnerX.setEnabled(enabled);
            cropSpinnerY.setEnabled(enabled);
            cropSpinnerWidth.setEnabled(enabled);
            cropSpinnerHeight.setEnabled(enabled);
        });
    }
}