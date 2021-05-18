package com.leoschulmann.podpishiplz.worker;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

@Slf4j
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
            log.info("File {} loaded.", file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        DocumentController.addFileToDocument(pdDocument, file, selectedPages);
        diag.dispose();
        System.gc();
        return null;
    }
}
