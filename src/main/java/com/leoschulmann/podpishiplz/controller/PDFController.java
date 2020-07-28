package com.leoschulmann.podpishiplz.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFController {
    public static BufferedImage[] loadPDF(String file) throws IOException {
        PDDocument pdDocument = PDDocument.load(new File(file));
        BufferedImage[] images = new BufferedImage[pdDocument.getNumberOfPages()];
        PDFRenderer renderer = new PDFRenderer(pdDocument);
        for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
            images[i] = renderer.renderImageWithDPI(i, 6.4f);    // for 75 px ..?
        }
        pdDocument.close();
        return images;
    }

    public static BufferedImage get300dpiPage(String file, int page) throws IOException {
        PDDocument pdDocument = PDDocument.load(new File(file));
        PDFRenderer renderer = new PDFRenderer(pdDocument);
        BufferedImage bufferedImage = renderer.renderImageWithDPI(page, 300f);
        pdDocument.close();
        return bufferedImage;
    }
}
