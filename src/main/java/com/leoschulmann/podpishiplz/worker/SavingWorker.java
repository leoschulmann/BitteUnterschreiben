package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.controller.PDFController;
import com.leoschulmann.podpishiplz.model.Page;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class SavingWorker extends AbstractUnterschreibenWorker {
    private final String file;
    private final float jpgQ;
    private final Class<? extends CompositeContext> blender;
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang");


    public SavingWorker(String file, float jpegQuality, JFrame owner, Class<? extends CompositeContext> blender) {
        super(owner, bundle.getString("rendering"));
        this.file = file;
        this.jpgQ = jpegQuality;
        this.blender = blender;
    }

    @Override
    protected Void doInBackground() {

        PDDocument pdf = PDFController.createPdf();
        int size = DocumentController.getAllPages().size();
        for (int i = 0; i < size; i++) {
            Page p = DocumentController.getAllPages().get(i);
            diag.setText((int) (1. * i / size * 100) + "%");
            DocumentController.renderPage(blender, p);
            try {
                PDFController.addPage(pdf, p, jpgQ);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.setRenderedImage(null);
            p.setImage(null);
        }
        try {
            pdf.save(file);
            pdf.close();
            LoggerFactory.getLogger(SavingWorker.class).info("File {} saved.", file);
        } catch (IOException e) {
            LoggerFactory.getLogger(SavingWorker.class).error(e.getMessage(), e);
        }

        System.gc();
        diag.dispose();
        return null;
    }
}
