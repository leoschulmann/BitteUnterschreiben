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
    public static void loadPDF(String file) throws IOException {
        PDDocument pdDocument = PDDocument.load(new File(file));
        BufferedImage[] images = new BufferedImage[pdDocument.getNumberOfPages()];
        PDFRenderer renderer = new PDFRenderer(pdDocument);
        for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
            images[i] = renderer.renderImageWithDPI(i, 6.4f);    // for 75 px ..?
        }
        pdDocument.close();
        GUIController.generateThumbnailButtons(images, file);
    }

    public static BufferedImage get300dpiPage(File file, int page)  {
        PDDocument pdDocument;
        BufferedImage bufferedImage = null;
        try {
            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            bufferedImage = renderer.renderImageWithDPI(page, 300f);
            pdDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static PDDocument prepareToSavePDF(float jpegQuality) throws IOException {
        PDDocument pdf = new PDDocument();
        pdf.getDocumentInformation().setCreator("BitteUnterschreiben v 0.1");
        for (BufferedImage im : DocumentController.getRenders()) {
            PDPage page = new PDPage(PDRectangle.A4);
            pdf.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, false);
            PDImageXObject imageXObject = JPEGFactory.createFromImage(pdf, im, jpegQuality);
            contentStream.drawImage(imageXObject, 0,0,
                    imageXObject.getWidth()/300f * 72, imageXObject.getHeight()/300f * 72);
            contentStream.close();
        }
        return pdf;
    }
}
