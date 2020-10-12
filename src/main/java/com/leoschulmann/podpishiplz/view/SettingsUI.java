package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventListener;
import com.leoschulmann.podpishiplz.controller.EventType;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.*;

public class SettingsUI extends JPanel implements SettingsTab, EventListener {
    private static JSlider zoomMultiplier;
    private static JCheckBox invertZoom;
    private static JTextField color;
    private static JSpinner maxOverlays;
    private static JComboBox<String> languageSelector;
    private static Map<String, String> langs;
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
    private static JLabel maxOverlaysLabel = new JLabel(bundle.getString("max.overlays.in.panel"));
    private static JLabel invertMouseZoomLabel = new JLabel(bundle.getString("invert.mouse.wheel.zoom"));
    private static JLabel mouseWheelZoomLabel = new JLabel(bundle.getString("mouse.wheel.zoom.speed"));
    private static JLabel selectionColorLabel = new JLabel(bundle.getString("selection.color"));
    private static JLabel languageLabel = new JLabel(bundle.getString("language"));

    SettingsUI() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.LINE_START;
        init();
        add(maxOverlaysLabel, g);
        g.gridx++;
        g.gridwidth = 2;
        add(maxOverlays, g);
        g.gridx = 0;
        g.gridy++;
        g.gridwidth = 1;
        add(invertMouseZoomLabel, g);
        g.gridx++;
        g.gridwidth = 2;
        add(invertZoom, g);
        g.gridx = 0;
        g.gridy++;
        g.gridwidth = 1;
        add(mouseWheelZoomLabel, g);
        g.gridx++;
        g.gridwidth = 2;
        add(zoomMultiplier, g);
        g.gridx = 0;
        g.gridy++;
        g.gridwidth = 1;
        add(selectionColorLabel, g);
        g.gridx++;
        add(new JLabel("#"), g);
        g.gridx++;
        add(color, g);
        g.gridx = 0;
        g.gridy++;
        add(languageLabel, g);
        g.gridx++;
        g.gridwidth = 2;
        add(languageSelector, g);
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
        color.setToolTipText(bundle.getString("color.of.selection.frame"));

        maxOverlays = new JSpinner();
        maxOverlays.setModel(new SpinnerNumberModel(20, 1, 100, 1));
        maxOverlays.setValue(SettingsController.getMaxOverlays());

        langs = new LinkedHashMap<>();
        langs.put("en", bundle.getString("english"));
        langs.put("ru", bundle.getString("russian"));
        languageSelector = new JComboBox<>(langs.values().toArray(new String[0]));
        languageSelector.setSelectedItem(langs.get(SettingsController.getLanguage()));
    }

    @Override
    public void saveState() {
        float z = (zoomMultiplier.getValue() + 100) / 100f;
        int mo = (int) maxOverlays.getValue();
        Optional<Map.Entry<String, String>> opt = langs.entrySet().stream()
                .filter(e -> e.getValue().equals(languageSelector.getSelectedItem()))
                .findFirst();
        String key = opt.isPresent() ? opt.get().getKey() : "en";

        if (SettingsController.getMaxOverlays() != mo) {
            SettingsController.setMaxOverlays(mo);
            EventController.notify(EventType.REFRESH_OVERLAYS_PANEL, null); //no reason to refresh if unchanged
        }
        SettingsController.setZoomSpeed(z);
        SettingsController.setInvertZoom(invertZoom.isSelected());
        SettingsController.setSelectionColor(color.getText());
        SettingsController.setLanguage(key);
        LoggerFactory.getLogger(SettingsGraphics.class).debug(
                "Saving data: max overlays {}, zoom {}, invert '{}', color #{}, language '{}'",
                mo, z, invertZoom.isSelected(), color.getText(), key);
    }

    @Override
    public String getTitle() {
        return bundle.getString("interface");
    }

    @Override
    public void eventUpdate(EventType event, Object object) {
        if (event == EventType.LOCALE_CHANGED) {
            bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
            maxOverlaysLabel.setText(bundle.getString("max.overlays.in.panel"));
            invertMouseZoomLabel.setText(bundle.getString("invert.mouse.wheel.zoom"));
            mouseWheelZoomLabel.setText(bundle.getString("mouse.wheel.zoom.speed"));
            selectionColorLabel.setText(bundle.getString("selection.color"));
            languageLabel.setText(bundle.getString("language"));
        }
    }
}
