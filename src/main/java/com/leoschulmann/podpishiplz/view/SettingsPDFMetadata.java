package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class SettingsPDFMetadata extends JPanel implements SettingsTab{
    private static JTextField producerField;
    private static JTextField creatorField;
    private static JCheckBox producerOverrideCB;

    public SettingsPDFMetadata() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.LINE_START;
        init();
        add(new JLabel("PDF Creator"), g);
        g.gridx++;
        add(creatorField, g);
        g.gridx = 0;
        g.gridy++;
        add(new JLabel("PDF Producer"), g);
        g.gridx++;
        add(producerField, g);
        g.gridx = 0;
        g.gridy++;
        add(new JLabel("Override 'Producer'"), g);
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
}
