package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderMultiply;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.MainPanel;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIController {
    private static MainPanel mainPanel;
    private static TopScrollerPanel topScrollerPanel;

    public static void openOption(AppWindow appWindow) {
        try {
            FileIOController.openPdfFile(appWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            DocumentController.renderAllPages(BlenderMultiply.class);  //todo hardcoded for now
            FileIOController.saveFile(appWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setTopScrollerPanel(TopScrollerPanel topScrollerPanel) {
        GUIController.topScrollerPanel = topScrollerPanel;
    }

    public static void generateThumbnailButtons(BufferedImage[] thumbs, String file) {
        for (int sourcePdfPage = 0; sourcePdfPage < thumbs.length; sourcePdfPage++) {
            BufferedImage th = thumbs[sourcePdfPage];
            Page documentPage = DocumentController.addNewPageToDocument(th, file, sourcePdfPage);
            JButton jb = topScrollerPanel.addNewButton(th);
            GUIController.addActionToThumbnailButton(jb, documentPage);}
        }
}
