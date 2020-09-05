package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.view.OverlayPanel;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class OverlaysPanelController {
    private static OverlayPanel panel;

    public static void removeAll() {
        panel.removeAll();
    }

    public static void loadThumbs(List<File> files) {
        for (File f : files) {
            try {
                JButton jb = new JButton(new ImageIcon(FileIOController.getOverlayThumb(f)));
                jb.setToolTipText(f.toString());
                jb.setVerticalTextPosition(SwingConstants.BOTTOM);
                jb.addActionListener(e -> {
                    FileIOController.loadOverlay(f.toString());
                    MainPanelController.repaint();
                });
                panel.put(jb);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(),
                        "Can't find file\n" + f.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                SettingsController.removeOverlayFromList(f);
                e.printStackTrace();
            }
        }
    }

    public static void revalidateAndRepaint() {
        panel.revalidate();
        panel.repaint();
    }


    public static void setPanel(OverlayPanel panel) {
        OverlaysPanelController.panel = panel;
    }

    public static void disableAll() {
        Arrays.stream(panel.getComponents()).forEach(component -> component.setEnabled(false));
    }

    public static void enableAll() {
        Arrays.stream(panel.getComponents()).forEach(component -> component.setEnabled(true));
    }
}
