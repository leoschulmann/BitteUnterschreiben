package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderDarken;
import com.leoschulmann.podpishiplz.graphics.BlenderMultiply;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.MainPanel;
import com.leoschulmann.podpishiplz.view.SettingsDialogue;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUIController {
    private static MainPanel mainPanel;
    private static TopScrollerPanel topScrollerPanel;
    private static SettingsDialogue settingsDialogue;

    public static void openOption(AppWindow appWindow) {
            FileIOController.openPdfFile(appWindow);
    }

    public static void placeOption(JFrame appWindow) {
        FileIOController.loadOverlay(appWindow);
        mainPanel.repaint();
    }

    public static void deleteSelectedOverlayOption() {
        DocumentController.removeSelectedOverlay();
        mainPanel.repaint();
    }

    public static void setMainPanel(MainPanel mainPanel) {
        GUIController.mainPanel = mainPanel;
    }

    private static void addActionToThumbnailButton(JButton jb, Page page) {
        jb.addActionListener(e -> GUIController.openPage(page));
    }

    private static void openPage(Page page) {
        if (DocumentController.getDoc().contains(page)) {
            DocumentController.setCurrentPage(page);
            mainPanel.repaint();
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
            DocumentController.renderAllPages(blender);
            FileIOController.saveFile(appWindow);
    }

    public static void setTopScrollerPanel(TopScrollerPanel topScrollerPanel) {
        GUIController.topScrollerPanel = topScrollerPanel;
    }

    public static void generateThumbnailButtons(BufferedImage[] thumbs, String file) {
        for (int sourcePdfPage = 0; sourcePdfPage < thumbs.length; sourcePdfPage++) {
            BufferedImage th = thumbs[sourcePdfPage];
            Page documentPage = DocumentController.addNewPageToDocument(th, file, sourcePdfPage);
            JButton jb = topScrollerPanel.addNewButton(th);
            GUIController.addActionToThumbnailButton(jb, documentPage);
        }
    }

    public static void openSettingsDialogue(AppWindow appWindow) {
        if (settingsDialogue == null) {
            settingsDialogue = new SettingsDialogue(appWindow);
        }
        settingsDialogue.setVisible(true);
    }
}
