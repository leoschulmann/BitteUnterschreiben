package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.view.MainPanel;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIController {
    private static MainPanel mainPanel;

    public static void openOption(AppWindow appWindow, TopScrollerPanel topScrollerPanel) {
        String file = FilePicker.openPDF(appWindow);
        if (file != null) {
            try {
                BufferedImage[] thumbs = PDFController.loadPDF(file);
                for (int sourcePdfPage = 0; sourcePdfPage < thumbs.length; sourcePdfPage++) {
                    BufferedImage th = thumbs[sourcePdfPage];
                    Page documentPage = DocumentController.addNewPageToDocument(th, file, sourcePdfPage);
                    JButton jb = topScrollerPanel.addNewButton(th);
                    GUIController.addActionToThumbnailButton(jb, documentPage);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void placeOption(JFrame appWindow) {
        FileIOController.loadOverlay(appWindow);
//        String file = FilePicker.openOverlay(appWindow);
//        if (file != null) {
//            DocumentController.addNewOverlay(file);
            mainPanel.repaint();
//        }
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
}
