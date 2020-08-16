package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.view.MainPanel;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIController {
    static MainPanel mainPanel;

    public static void openOption(AppWindow appWindow, TopScrollerPanel topScrollerPanel) {
        String file = FilePicker.openPDF(appWindow);
        if (file != null) {
            try {
                BufferedImage[] thumbs = PDFController.loadPDF(file);
                for (int filePageNumber = 0; filePageNumber < thumbs.length; filePageNumber++) {
                    BufferedImage th = thumbs[filePageNumber];
                    int documentPageNumber = DocumentController.addNewPageToDocument(th, file, filePageNumber);
                    JButton jb = topScrollerPanel.addNewButton(th);
                    GUIController.addActionToThumbnailButton(jb, documentPageNumber);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void placeOption(AppWindow appWindow) {
        String file = FilePicker.openOverlay(appWindow);
        if (file != null) {
            try {
                mainPanel.addNewOverlay(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainPanel.repaint();
        }
    }

    public static void setMainPanel(MainPanel mainPanel) {
        GUIController.mainPanel = mainPanel;
    }

    public static void addActionToThumbnailButton(JButton jb, int page) {
        jb.addActionListener(e -> {
            try {
                GUIController.openPage(page);
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, e.getClass().toString()
                        + " " + ioException.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                ioException.printStackTrace();
            }
        });
    }

    private static void openPage(int page) throws IOException {
        mainPanel.setBufferedImage(DocumentController.getPage(page).getImage());
        mainPanel.repaint();
    }
}
