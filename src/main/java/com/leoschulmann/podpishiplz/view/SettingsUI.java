package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SettingsUI extends JPanel implements SettingsTab {
    private static JSlider zoomMultiplier;
    private static JCheckBox invertZoom;
    private static JTextField color;

    public SettingsUI() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.LINE_START;
        init();

        add(new JLabel("Invert mouse wheel zoom"), g);
        g.gridx++;
        g.gridwidth = 2;
        add(invertZoom, g);
        g.gridx = 0;
        g.gridy++;
        g.gridwidth = 1;
        add(new JLabel("Mouse wheel zoom speed"), g);
        g.gridx++;
        g.gridwidth = 2;
        add(zoomMultiplier, g);
        g.gridx = 0;
        g.gridy++;
        g.gridwidth = 1;
        add(new JLabel("Selection color"), g);
        g.gridx++;
        add(new JLabel("#"), g);
        g.gridx++;
        add(color, g);
    }

    @Override
    public void init() {
        //zoom multiplier real range is 1.01 .. 1.10
        zoomMultiplier = new JSlider(JSlider.HORIZONTAL, 1, 10, (int) (SettingsController.getZoomSpeed() * 100 - 100));
        zoomMultiplier.setMajorTickSpacing(1);
        zoomMultiplier.setSnapToTicks(true);
        zoomMultiplier.setPaintTicks(true);

        invertZoom = new JCheckBox();
        invertZoom.setSelected(SettingsController.isInvertZoom());

        color = new JTextField(5);
        color.setText(SettingsController.getSelectionColor());
        color.setBorder(BorderFactory.createLineBorder(Color.decode('#' + color.getText())));

        color.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!SettingsController.colorVerify(color.getText())) {
                    color.setText(SettingsController.DEFAULT_COLOR);
                }
                color.setBorder(BorderFactory.createLineBorder(Color.decode('#' + color.getText())));
            }
        });
        color.setToolTipText("Color of selection frame in hexadecimal format. " +
                "000000 - black, FFFFFF - white, FF00FF - magenta, etc");
    }

    @Override
    public void saveState() {
        float z = (zoomMultiplier.getValue() + 100) / 100f;
        SettingsController.setZoomSpeed(z);
        SettingsController.setInvertZoom(invertZoom.isSelected());
        SettingsController.setSelectionColor(color.getText());
        LoggerFactory.getLogger(SettingsGraphics.class).debug(
                "Saving data: zoom {}, invert '{}', color #{}", z, invertZoom.isSelected(), color.getText());

    }
}
