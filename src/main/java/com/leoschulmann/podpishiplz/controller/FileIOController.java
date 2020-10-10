package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.worker.AbstractUnterschreibenWorker;
import com.leoschulmann.podpishiplz.worker.OpeningWorker;
import com.leoschulmann.podpishiplz.worker.SavingWorker;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FileIOController {
    public static void loadOverlay(File file) {
        LoggerFactory.getLogger(FileIOController.class).info("Loading overlay {}", file.toString());
        try {
            BufferedImage im = ImageIO.read(file);
            if (im.getType() != BufferedImage.TYPE_INT_ARGB) {  // converting to int raster. byte raster won't work
                BufferedImage newImage = new BufferedImage(
                        im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = newImage.createGraphics();
                g.drawImage(im, 0, 0, null);
                g.dispose();
                im = newImage;
            }
            EventController.notify(EventType.OVERLAY_LOADED_FROM_DISK, file);
            EventController.notify(EventType.REFRESH_OVERLAYS_PANEL, null);
            DocumentController.addNewOverlay(im);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(), e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            LoggerFactory.getLogger(FileIOController.class).error(e.getMessage(), e);
        }
    }


    public static void blendAndSavePdfFile(float jpegQuality, Class<? extends CompositeContext> blender, String file) {
            SavingWorker worker = new SavingWorker(file, jpegQuality, BitteUnterschreiben.getApp(), blender);
            worker.execute();
            worker.runDialog();
    }

    public static void openPdfFile(String file, boolean[] selectedPages) {
        LoggerFactory.getLogger(FileIOController.class).debug("Loading file: {}", file);
        AbstractUnterschreibenWorker worker = new OpeningWorker(BitteUnterschreiben.getApp(), file, selectedPages);
        worker.execute();
        worker.runDialog();
    }

    public static BufferedImage getOverlayIm(File f) {
        LoggerFactory.getLogger(FileIOController.class).info("Loading thumbnail for {}", f.getName());
        try {
            return ImageIO.read(f);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(), e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            LoggerFactory.getLogger(FileIOController.class).error(e.getMessage(), e);
            BufferedImage im = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics g = im.createGraphics();
            g.setColor(Color.RED);
            g.drawString("ERROR", 1, 25);
            g.dispose();
            return im;
        }
    }

    public static BufferedImage[] getPdfPreviews(String file)  {  //todo dirty. rewrite
        LoggerFactory.getLogger(FileIOController.class).info("Loading thumbnails for {}", file);
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean[] selectedPages = new boolean[pdDocument.getNumberOfPages()];
        Arrays.fill(selectedPages, true);
        return PDFController.generatePageThumbnails(pdDocument, selectedPages);
    }
}
