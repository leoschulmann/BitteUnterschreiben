package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.view.PageSelectorElement;
import com.leoschulmann.podpishiplz.worker.AbstractUnterschreibenWorker;
import com.leoschulmann.podpishiplz.worker.OpeningWorker;
import com.leoschulmann.podpishiplz.worker.SavingWorker;
import com.leoschulmann.podpishiplz.worker.WorkerDialog;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class FileIOController {
    static void loadOverlay(File file) {
        log.info("Loading overlay {}", file.toString());
        try {
            BufferedImage im = ImageIO.read(file);
            if (im == null) throw new IllegalArgumentException("Can't load file " + file.toString());
            // converting to int raster. byte raster won't work
            int maxDimension = (int) Math.sqrt(Math.pow(im.getWidth(), 2) + Math.pow(im.getHeight(), 2));
            //newImage: each side equals diagonal of the original BufferedImage (prevents clipping on rotation)
            BufferedImage newImage = new BufferedImage(maxDimension, maxDimension, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            AffineTransform at = new AffineTransform();
            int translateX = (maxDimension - im.getWidth()) / 2;
            int translateY = (maxDimension - im.getHeight()) / 2;
            at.translate(translateX, translateY);
            g.setTransform(at);
            g.drawImage(im, 0, 0, null);
            g.dispose();
            im = newImage;
            EventController.notify(EventType.OVERLAY_LOADED_FROM_DISK, file);
            EventController.notify(EventType.REFRESH_OVERLAYS_PANEL, null);
            DocumentController.addNewOverlay(im);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(), e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(), e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    static void blendAndSavePdfFile(float jpegQuality, Class<? extends CompositeContext> blender, String file) {
            SavingWorker worker = new SavingWorker(file, jpegQuality, BitteUnterschreiben.getApp(), blender);
            worker.execute();
            worker.runDialog();
    }

    static void openPdfFile(String file, boolean[] selectedPages) {
        log.debug("Loading file: {}", file);
        AbstractUnterschreibenWorker worker = new OpeningWorker(BitteUnterschreiben.getApp(), file, selectedPages);
        worker.execute();
        worker.runDialog();
    }

    static PageSelectorElement[] loadPreviewsFromFile(String file) {
        WorkerDialog splash = new WorkerDialog(BitteUnterschreiben.getApp(), "");
        splash.setVisible(true);
        final PageSelectorElement[][] elements = new PageSelectorElement[1][1];

        Thread t = new Thread(() -> {
            PDDocument pdDocument = null;
            try {
                pdDocument = PDDocument.load(new File(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean[] allPages = new boolean[pdDocument.getNumberOfPages()];
            Arrays.fill(allPages, true);
            BufferedImage[] imgs = PDFController.generatePageThumbnails(pdDocument, allPages);
            elements[0] = new PageSelectorElement[imgs.length];
            for (int i = 0; i < imgs.length; i++) {
                elements[0][i] = new PageSelectorElement(imgs[i]);
            }
        });
        t.start();

        try {
            t.join();
            splash.setVisible(false);
            splash.dispose();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return elements[0];
    }

    static BufferedImage getOverlayIm(File f) {
        log.info("Loading thumbnail for {}", f.getName());
        try {
            return ImageIO.read(f);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(), e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            log.error(e.getMessage(), e);
            BufferedImage im = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics g = im.createGraphics();
            g.setColor(Color.RED);
            g.drawString("ERROR", 1, 25);
            g.dispose();
            return im;
        }
    }
}
