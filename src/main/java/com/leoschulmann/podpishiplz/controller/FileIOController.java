package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.worker.AbstractUnterschreibenWorker;
import com.leoschulmann.podpishiplz.worker.OpeningWorker;
import com.leoschulmann.podpishiplz.worker.SavingWorker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileIOController {
    public static void loadOverlay(JFrame appWindow) {  //todo exc handling here
        String file = FilePicker.openOverlay(appWindow);
        if (file != null) {
            try {
                BufferedImage im = ImageIO.read(new File(file));
                if (im.getType() != BufferedImage.TYPE_INT_ARGB) {  // converting to int raster. byte raster won't work
                    BufferedImage newImage = new BufferedImage(
                            im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = newImage.createGraphics();
                    g.drawImage(im, 0, 0, null);
                    g.dispose();
                    im = newImage;
                }
                DocumentController.addNewOverlay(im);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void blendAndSavePdfFile(JFrame appWindow, float jpegQuality, Class<? extends CompositeContext> blender) {
        String file = FilePicker.savePDF(appWindow);
        if (file != null) {
            SavingWorker worker = new SavingWorker(file, jpegQuality, appWindow, blender);
            worker.execute();
            worker.runDialog();
        }
    }

    public static void openPdfFile(AppWindow appWindow)  {
        String file = FilePicker.openPDF(appWindow);
        if (file != null) {
            AbstractUnterschreibenWorker worker = new OpeningWorker(appWindow, file);
            worker.execute();
            worker.runDialog();
        }
    }
}
