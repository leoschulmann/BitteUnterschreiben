package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.EventType;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsGraphics extends JPanel implements SettingsTab {
    private static JSlider jpegQltySlider;
    private static JSlider dpiSlider;
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
    private static JLabel jpegCompressionLabel = new JLabel(bundle.getString("jpeg.compression"));
    private static JLabel resolutionLabel = new JLabel(bundle.getString("resolution.dpi"));

    public SettingsGraphics() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.LINE_START;
        init();

        add(jpegCompressionLabel, g);
        g.gridx++;
        add(jpegQltySlider, g);
        g.gridx = 0;
        g.gridy++;
        add(resolutionLabel, g);
        g.gridx++;
        add(dpiSlider, g);
    }

    public void init() {
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

    }

    public void saveState() {
        float q = 1.0f * jpegQltySlider.getValue() / 100;
        float r = 1.0f * dpiSlider.getValue() / 300;
        SettingsController.setJpegQuality(q);
        SettingsController.setResolutionMultiplier(r);
        LoggerFactory.getLogger(SettingsGraphics.class).debug(
                "Saving data: quality {}, resolution {}", q, r);
    }

    @Override
    public String getTitle() {
        return bundle.getString("graphics");
    }

    @Override
    public void eventUpdate(EventType event, Object object) {
        if (event == EventType.LOCALE_CHANGED) {
            bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
            jpegCompressionLabel.setText(bundle.getString("jpeg.compression"));
            resolutionLabel.setText(bundle.getString("resolution.dpi"));

        }
    }
}
