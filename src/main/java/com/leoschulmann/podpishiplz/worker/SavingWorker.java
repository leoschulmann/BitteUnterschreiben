package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.PDFController;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;

public class SavingWorker extends SwingWorker<Void, Void> {
    private String file;
    private float jpgQ;
    private JFrame owner;
    private JDialog dialog;

    public SavingWorker(String file, float jpegQuality, JFrame owner) {
        this.file = file;
        this.jpgQ = jpegQuality;
        this.owner = owner;
        this.dialog = new WorkerDialog(owner, "Saving...");
    }

    public void runDialog() {
        dialog.setVisible(true);
    }

    @Override
    protected Void doInBackground() throws Exception {
        PDDocument pdf = PDFController.buildPDF(jpgQ);
        pdf.save(file);
        pdf.close();
        dialog.dispose();
        return null;
    }
}
