package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class SettingsGraphics extends JPanel {
    public static JSlider jpegQltySlider;
    public static JSlider dpiSlider;
    public static JSlider zoomMultiplier;
    public static JCheckBox invertZoom;
    public static JTextField color;


    public SettingsGraphics() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.LINE_START;
        init();

        add(new JLabel("JPEG Compression"), g);
        g.gridx++;
        add(jpegQltySlider, g);
        g.gridx = 0;
        g.gridy++;
        add(new JLabel("Resolution, dpi"), g);
        g.gridx++;
        add(dpiSlider, g);
        g.gridx = 0;
        g.gridy++;
        add(new JLabel("Invert mouse wheel zoom"), g);
        g.gridx++;
        add(invertZoom, g);
        g.gridx = 0;
        g.gridy++;
        add(new JLabel("Mouse wheel zoom speed"), g);
        g.gridx++;
        add(zoomMultiplier, g);
    }

    private void init() {
        jpegQltySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (SettingsController.getJpegQuality() * 100));
        jpegQltySlider.setMajorTickSpacing(20);
        jpegQltySlider.setMinorTickSpacing(10);
        jpegQltySlider.setSnapToTicks(true);
        jpegQltySlider.setPaintTicks(true);
        jpegQltySlider.setPaintLabels(true);

        dpiSlider = new JSlider(JSlider.HORIZONTAL, 100, 600, (int) (SettingsController.getResolutionMultiplier() * 300));
        dpiSlider.setMajorTickSpacing(100);
        dpiSlider.setMinorTickSpacing(50);
        dpiSlider.setSnapToTicks(true);
        dpiSlider.setPaintTicks(true);
        dpiSlider.setPaintLabels(true);

        //zoom multiplier real range is 1.01 .. 1.10
        zoomMultiplier = new JSlider(JSlider.HORIZONTAL, 1, 10, (int) (SettingsController.getZoomSpeed() * 100 - 100));
        zoomMultiplier.setMajorTickSpacing(1);
        zoomMultiplier.setSnapToTicks(true);
        zoomMultiplier.setPaintTicks(true);

        invertZoom = new JCheckBox();
        invertZoom.setSelected(SettingsController.isInvertZoom());

        color = new JTextField();
        color.setText(SettingsController.getSelectionColor());

    }

    public void saveState() {
        float q = 1.0f * jpegQltySlider.getValue() / 100;
        float r = 1.0f * dpiSlider.getValue() / 300;
        float z = (zoomMultiplier.getValue() + 100) / 100f;
        SettingsController.setJpegQuality(q);
        SettingsController.setResolutionMultiplier(r);
        SettingsController.setZoomSpeed(z);
        SettingsController.setInvertZoom(invertZoom.isSelected());
        LoggerFactory.getLogger(SettingsGraphics.class).debug(
                "Saving data: quality {}, resolution {}, zoom {}, invert '{}'", q, r, z, invertZoom.isSelected());
    }
}
