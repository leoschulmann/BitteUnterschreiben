package com.leoschulmann.podpishiplz.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
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

    public static BufferedImage get300dpiPage(File file, int page) throws IOException {
        PDDocument pdDocument = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(pdDocument);
        BufferedImage bufferedImage = renderer.renderImageWithDPI(page, 300f);
        pdDocument.close();
        return bufferedImage;
    }

    public static void savePDF(String file) throws IOException {
        PDDocument pdf = new PDDocument();
        for (BufferedImage im : DocumentController.getRenders()) {
            PDPage page = new PDPage(PDRectangle.A4);
            pdf.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, false);
            PDImageXObject imageXObject = JPEGFactory.createFromImage(pdf, im);
            contentStream.drawImage(imageXObject, 0,0,
                    imageXObject.getWidth()/300f * 72, imageXObject.getHeight()/300f * 72);
            contentStream.close();
        }
        pdf.save(new File(file));
        pdf.close();
    }
}
