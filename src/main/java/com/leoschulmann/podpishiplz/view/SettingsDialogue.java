package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsDialogue extends JDialog {
    private final SettingsGraphics graphicsSettings;
    private final SettingsPDFMetadata pdfMetadataSettings;
    private final SettingsBlending blendingSettings;
    private final SettingsUI userInterfaceSettings;
    private final java.util.List<SettingsTab> tabs;

    public SettingsDialogue(Frame owner) {
        super(owner);
        setName("Settings");
        setModal(true);
        setResizable(false);

        tabs = new ArrayList<>();

        tabs.add(graphicsSettings = new SettingsGraphics());
        tabs.add(userInterfaceSettings = new SettingsUI());
        tabs.add(blendingSettings = new SettingsBlending());
        tabs.add(pdfMetadataSettings = new SettingsPDFMetadata());

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Graphics", graphicsSettings);
        tabs.add("Interface", userInterfaceSettings);
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
        tabs.forEach(SettingsTab::saveState);

        try {
            SettingsController.saveYML();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LoggerFactory.getLogger(SettingsDialogue.class).error(e.getMessage(), e);
        }
        setVisible(false);
    }
}
