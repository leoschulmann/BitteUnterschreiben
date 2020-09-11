package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.controller.PDFController;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SavingWorker extends AbstractUnterschreibenWorker {
    private final String file;
    private final float jpgQ;
    private final Class<? extends CompositeContext> blender;

    public SavingWorker(String file, float jpegQuality, JFrame owner, Class<? extends CompositeContext> blender) {
        super(owner, "Рендеринг...");
        this.file = file;
        this.jpgQ = jpegQuality;
        this.blender = blender;
    }

    @Override
    protected Void doInBackground() {
        DocumentController.renderAllPages(blender);
        diag.setText("Сохранение...");
        PDDocument pdf;
        try {
            pdf = PDFController.buildPDF(jpgQ);
            pdf.save(file);
            LoggerFactory.getLogger(SavingWorker.class).info("File {} saved.", file);
            pdf.close();
        } catch (IOException e) {
            LoggerFactory.getLogger(SavingWorker.class).error(e.getMessage(), e);
        }
        diag.dispose();
        return null;
    }
}
