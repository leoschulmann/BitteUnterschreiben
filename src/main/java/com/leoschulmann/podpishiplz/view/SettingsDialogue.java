package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SettingsDialogue extends JDialog {
    private final SettingsGraphics graphicsSettings;
    private final SettingsPDFMetadata pdfMetadataSettings;
    private final SettingsBlending blendingSettings;

    public SettingsDialogue(Frame owner) {
        super(owner);
        setName("Settings");
        setModal(true);
        setResizable(false);

        graphicsSettings = new SettingsGraphics();
        blendingSettings = new SettingsBlending();
        pdfMetadataSettings = new SettingsPDFMetadata();

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Graphics", graphicsSettings);
        tabs.add("PDF Metadata", pdfMetadataSettings);
        tabs.add("Blending", blendingSettings);

        JPanel okCancel = new JPanel();
        JButton cancel = new JButton("Cancel");
        JButton ok = new JButton("OK");
        getRootPane().setDefaultButton(ok);
        cancel.addActionListener(e -> this.setVisible(false));
        ok.addActionListener(e -> confirm());
        okCancel.add(ok);
        okCancel.add(cancel);

        add(tabs, BorderLayout.CENTER);
        add(okCancel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void confirm() {
        pdfMetadataSettings.saveState();
        graphicsSettings.saveState();
        blendingSettings.saveState();

        try {
            SettingsController.saveYML();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LoggerFactory.getLogger(SettingsDialogue.class).error(e.getMessage(), e);
        }
        setVisible(false);
    }
}
