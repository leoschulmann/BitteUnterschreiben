package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.graphics.Resizer;
import com.leoschulmann.podpishiplz.view.OverlayPanel;
import com.leoschulmann.podpishiplz.view.OverlayThumbButton;
import com.leoschulmann.podpishiplz.view.OverlayThumbButtonContextMenu;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class OverlaysPanelController {
    private static OverlayPanel panel;
    private static boolean disabled;
    private static final List<OverlayThumbButton> BUTTONS = new ArrayList<>();

    private static void removeAll() {
        panel.removeAll();
    }

    private static OverlayThumbButton loadThumb(File f) {
        try {
            BufferedImage raw = FileIOController.getOverlayIm(f);
            BufferedImage thumbnail = Resizer.resize(raw, 50);
            OverlayThumbButton butt = new OverlayThumbButton(thumbnail, f.toString(), f);
            butt.setVerticalTextPosition(SwingConstants.BOTTOM);
            butt.addActionListener(e -> GUIController.overlayThumbnailButtonOnClickAction(butt.getFile()));
            butt.addMouseListener(new MouseAdapter() {
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

            return butt;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(),
                    "Can't find file\n" + f.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            SettingsController.removeOverlayFromList(f);
            e.printStackTrace();
            return null;
        }
    }

    private static void placeButtons() {
        BUTTONS.forEach(panel::put);
    }

    private static void revalidateAndRepaint() {
        panel.revalidate();
        panel.repaint();
    }


    public static void setPanel(OverlayPanel panel) {
        OverlaysPanelController.panel = panel;
    }

    private static void disableAll() {
        Arrays.stream(panel.getComponents()).forEach(component -> component.setEnabled(false));
    }

    private static void enableAll() {
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
                    List<File> overlaysFromSettings = SettingsController.getUsedOverlays();
                    LoggerFactory.getLogger(OverlaysPanelController.class).debug("Loading {} overlays",
                            overlaysFromSettings.size());
                    List<OverlayThumbButton> cache = new ArrayList<>(BUTTONS);
                    BUTTONS.clear();

                    for (File f : overlaysFromSettings) {
                        Optional<OverlayThumbButton> opt = cache.stream().filter(b -> b.getFile().equals(f)).findAny();
                        BUTTONS.add((opt.orElseGet(() -> loadThumb(f))));
                    }
                    placeButtons();
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
