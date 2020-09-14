package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class OpeningWorker extends AbstractUnterschreibenWorker {
    private final String file;

    public OpeningWorker(JFrame owner, String file) {
        super(owner, "Opening...");
        this.file = file;
    }

    @Override
    protected Void doInBackground() {
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(new File(file));
            LoggerFactory.getLogger(OpeningWorker.class).info("File {} loaded.", file);
        } catch (IOException e) {
            LoggerFactory.getLogger(OpeningWorker.class).error(e.getMessage(), e);
        }
        DocumentController.addFileToDocument(pdDocument, file);
        diag.dispose();
        return null;
    }
}
