package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class OpeningWorker extends AbstractUnterschreibenWorker {
    private final String file;

    public OpeningWorker(JFrame owner, String file) {
        super(owner, "Файл открывается...");
        this.file = file;
    }

    @Override
    protected Void doInBackground() {
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DocumentController.addFileToDocument(pdDocument, file);
        diag.dispose();
        return null;
    }
}
