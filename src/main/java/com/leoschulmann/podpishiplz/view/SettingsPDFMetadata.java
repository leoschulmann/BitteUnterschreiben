package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.EventType;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsPDFMetadata extends JPanel implements SettingsTab {
    private static JTextField producerField;
    private static JTextField creatorField;
    private static JCheckBox producerOverrideCB;
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
    private static JLabel pdfCreatorLabel = new JLabel(bundle.getString("pdf.creator"));
    private static JLabel pdfProducerLabel = new JLabel(bundle.getString("pdf.producer"));
    private static JLabel overrideProducerLabel = new JLabel(bundle.getString("override.producer"));

    SettingsPDFMetadata() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.LINE_START;
        init();
        add(pdfCreatorLabel, g);
        g.gridx++;
        add(creatorField, g);
        g.gridx = 0;
        g.gridy++;
        add(pdfProducerLabel, g);
        g.gridx++;
        add(producerField, g);
        g.gridx = 0;
        g.gridy++;
        add(overrideProducerLabel, g);
        g.gridx++;
        add(producerOverrideCB, g);
    }

    public void init() {

        creatorField = new JTextField(25);
        creatorField.setText(SettingsController.getCreator());

        boolean overriding = SettingsController.isProducerOverride();
        producerField = new JTextField(25);
        producerOverrideCB = new JCheckBox();

        if (overriding) {
            producerOverrideCB.setSelected(true);
            String producer = SettingsController.getProducer();
            producerField.setText(producer);
            producerField.setEnabled(true);
        } else {
            producerOverrideCB.setSelected(false);
            producerField.setText(SettingsController.getDefaultProducer());
            producerField.setEnabled(false);
        }

        producerOverrideCB.addActionListener(e -> {
            if (producerOverrideCB.isSelected()) {
                producerField.setText(SettingsController.getProducer());
                producerField.setEnabled(true);
            } else {
                producerField.setText(SettingsController.getDefaultProducer());
                producerField.setEnabled(false);
            }
        });
    }

    public void saveState() {
        String p = producerOverrideCB.isSelected() ? producerField.getText() : null;
        String c = creatorField.getText();
        SettingsController.setProducerOverride(producerOverrideCB.isSelected());
        SettingsController.setProducer(p);
        SettingsController.setCreator(c);
        LoggerFactory.getLogger(SettingsPDFMetadata.class).debug(
                "Saving data: creator '{}', producer '{}', producer override '{}'", c, p, producerOverrideCB.isSelected());
    }

    @Override
    public String getTitle() {
        return bundle.getString("pdf.metadata");
    }

    @Override
    public void eventUpdate(EventType event, Object object) {
        if (event == EventType.LOCALE_CHANGED) {
            bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
            pdfCreatorLabel.setText(bundle.getString("pdf.creator"));
            pdfProducerLabel.setText(bundle.getString("pdf.producer"));
            overrideProducerLabel.setText(bundle.getString("override.producer"));

        }
    }
}
