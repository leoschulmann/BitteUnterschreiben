package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderDarken;
import com.leoschulmann.podpishiplz.graphics.BlenderMultiply;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.view.ThumbnailButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUIController {

    public static void openOption(AppWindow appWindow) {
        FileIOController.openPdfFile(appWindow);
    }

    public static void placeOption(JFrame appWindow) {
        String file = FilePicker.openOverlay(appWindow);
        if (file != null) {
            FileIOController.loadOverlay(file);
            MainPanelController.repaint();
        }
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

    public static void openSettingsDialogue() {
        SettingsController.openSettings();
    }
}
