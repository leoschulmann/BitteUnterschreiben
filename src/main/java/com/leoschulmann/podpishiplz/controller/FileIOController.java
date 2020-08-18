package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.FilePicker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileIOController {
    public static void loadOverlay(JFrame appWindow) {  //todo exc handling here
        String file = FilePicker.openOverlay(appWindow);
        if (file != null) {
            try {
                BufferedImage im = ImageIO.read(new File(file));
                DocumentController.addNewOverlay(im);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFile(JFrame appWindow) throws IOException {
        String file = FilePicker.savePDF(appWindow);
        PDFController.savePDF(file);
    }

    public static void openPdfFile(AppWindow appWindow) throws IOException {
        String file = FilePicker.openPDF(appWindow);
        if (file != null) {
            PDFController.loadPDF(file);
        }
    }
}
