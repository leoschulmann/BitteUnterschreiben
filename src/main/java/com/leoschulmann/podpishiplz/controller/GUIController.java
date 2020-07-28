package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.view.MainPanel;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIController {
    static MainPanel mainPanel;

    public static void openOption(AppWindow appWindow, TopScrollerPanel topScrollerPanel) {
        String file = FilePicker.open(appWindow);
        if (file != null) {
            try {
                topScrollerPanel.loadFile(file);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    public static void openPage(BufferedImage image) {
        mainPanel.setBufferedImage(image);
        mainPanel.repaint();
    }

    public static void setMainPanel(MainPanel mainPanel) {
        GUIController.mainPanel = mainPanel;
    }
}
