package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.view.AppWindow;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.io.File;

public class OpeningWorker extends SwingWorker<Void, Void> {
    private String file;
    private JFrame owner;
    private JDialog dialog;

    public OpeningWorker(String file, AppWindow appWindow) {
        this.file = file;
        this.owner = appWindow;
        this.dialog = new WorkerDialog(owner, "Opening...");
    }

    public void runDialog() {
        dialog.setVisible(true);
    }

    @Override
    protected Void doInBackground() throws Exception {
        PDDocument pdDocument = PDDocument.load(new File(file));
        DocumentController.addFileToDocument(pdDocument, file);
        dialog.dispose();
        return null;
    }
}
