package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class OpeningWorker extends AbstractUnterschreibenWorker {
    private final String file;
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang");
    private final boolean[] selectedPages;

    public OpeningWorker(JFrame owner, String file, boolean[] selectedPages) {
        super(owner, bundle.getString("opening"));
        this.file = file;
        this.selectedPages = selectedPages;
        addPropertyChangeListener(new OpeningWorkerListener());
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
        DocumentController.addFileToDocument(pdDocument, file, selectedPages);
        diag.dispose();
        System.gc();
        return null;
    }
}
