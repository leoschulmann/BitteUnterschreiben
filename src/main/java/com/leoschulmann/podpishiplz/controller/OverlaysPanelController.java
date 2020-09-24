package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.view.OverlayPanel;
import com.leoschulmann.podpishiplz.view.OverlayThumbButtonContextMenu;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class OverlaysPanelController {
    private static OverlayPanel panel;
    private static boolean disabled;

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
                jb.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {  //works on mac
                        if (e.isPopupTrigger()) {
                            new OverlayThumbButtonContextMenu(f).show(e.getComponent(), e.getX(), e.getY());
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) { // todo test on win
                        if (e.isPopupTrigger()) {
                            new OverlayThumbButtonContextMenu(f).show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
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

    public static void initListener() {
        EventListener el = (event, object) -> {
            switch (event) {
                case MAIN_PANEL_FULL:
                    enableAll();
                    disabled = false;
                    break;
                case MAIN_PANEL_EMPTY:
                case NO_PAGES_IN_DOCUMENT:
                    disableAll();
                    disabled = true;
                    break;
                case REFRESH_OVERLAYS_PANEL:
                    removeAll();
                    loadThumbs(SettingsController.getUsedOverlays());
                    revalidateAndRepaint();
                    if (disabled) disableAll();
                    break;
            }
        };
        EventController.subscribe(EventType.REFRESH_OVERLAYS_PANEL, el);
        EventController.subscribe(EventType.MAIN_PANEL_FULL, el);
        EventController.subscribe(EventType.MAIN_PANEL_EMPTY, el);
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, el);
    }
}
