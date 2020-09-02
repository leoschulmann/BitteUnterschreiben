package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.graphics.BlenderDarken;
import com.leoschulmann.podpishiplz.graphics.BlenderMultiply;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.ThumbnailButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUIController {
    private static final EventListener GUIControllerEventListener;

    static {
        GUIControllerEventListener = new EventListener() {
            final JButton welcomeBtn = new JButton("Открыть .pdf...");

            @Override
            public void eventUpdate(EventType event, Object object) {
                welcomeBtn.addActionListener(e -> openOption(BitteUnterschreiben.getApp()));
                switch (event) {
                    case NO_PAGES_IN_DOCUMENT:
                        placeButton(welcomeBtn);
                        openPage(null);
                        break;
                    case PAGES_ADDED:
                        remButton(welcomeBtn);
                        break;
                    case PAGES_REORDERED:
                        Page pageRemoved = (Page) object;
                        TopPanelController.getButtons().remove(
                                TopPanelController.getButtons().stream()
                                .filter(b -> b.getPage() == pageRemoved)
                                .findFirst()
                                .orElse(null));
                        TopPanelController.removeAll();
                        TopPanelController.clearAndPlaceThumbnailsOrdered();
                        TopPanelController.revalidateAndRepaint();
                        break;
                }
            }
        };
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, GUIControllerEventListener);
        EventController.subscribe(EventType.PAGES_ADDED, GUIControllerEventListener);
        EventController.subscribe(EventType.PAGES_REORDERED, GUIControllerEventListener);
    }

    public static void openOption(AppWindow appWindow) {
        FileIOController.openPdfFile(appWindow);
    }

    public static void placeOption(JFrame appWindow) {
        FileIOController.loadOverlay(appWindow);
        MainPanelController.repaint();
    }

    public static void deleteSelectedOverlayOption() {
        DocumentController.removeSelectedOverlay();
        MainPanelController.repaint();
    }

    public static void openPage(Page page) {
        if (page == null) {
            DocumentController.setCurrentPage(null);
            MainPanelController.repaint();
            EventController.notify(EventType.MAIN_PANEL_EMPTY, null);
        } else if (DocumentController.contains(page)) {
            DocumentController.setCurrentPage(page);
            MainPanelController.repaint();
            EventController.notify(EventType.MAIN_PANEL_FULL, null);
            page.getOverlays().forEach(overlay -> overlay.setSelected(false));
            EventController.notify(EventType.OVERLAY_DESELECTED, null);
        }
    }

    public static void saveFile(JFrame appWindow) {
        Class<? extends CompositeContext> blender = null;
        switch (SettingsController.getBlendingMode()) {
            case (1):
                blender = BlenderDarken.class;
                break;
            case (2):
                blender = BlenderMultiply.class;
                break;
            default:
        }
        FileIOController.blendAndSavePdfFile(appWindow, SettingsController.getJpegQuality(), blender);
    }

    public static ThumbnailButton generateThumbnailButton(BufferedImage thumbnail, Page p) {
        ThumbnailButton jb = new ThumbnailButton(thumbnail, p);
        jb.setVerticalTextPosition(SwingConstants.BOTTOM);
        jb.setHorizontalTextPosition(SwingConstants.CENTER);
        jb.addActionListener(e -> GUIController.openPage(jb.getPage()));
        TopPanelController.getButtons().add(jb);
        return jb;
    }

    public static void placeButton(JButton jb) {
        TopPanelController.put(jb);
        TopPanelController.revalidateAndRepaint();
    }

    public static void remButton(JButton jb) {
        TopPanelController.remove(jb);
        TopPanelController.revalidateAndRepaint();
    }

    public static void openSettingsDialogue() {
        SettingsController.openSettings();
    }
}
