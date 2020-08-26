package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.model.Page;
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
        for (Page pg : DocumentController.getDoc().getPages()) {
            int width = pg.getMediaWidth();
            int height = pg.getMediaHeight();
            PDPage page = new PDPage(new PDRectangle(width, height));
            pdf.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, false);
            PDImageXObject imXObj = JPEGFactory.createFromImage(pdf, pg.getRenderedImage(), jpegQuality);
            contentStream.drawImage(imXObj, 0, 0, width, height);
            contentStream.close();
        }
        return pdf;
    }

    public static PDRectangle getMediabox(File originalFile, int originalFilePageNumber) {
        PDDocument pdDocument;
        PDRectangle rect = null;
        try {
            pdDocument = PDDocument.load(originalFile);
            rect = pdDocument.getPage(originalFilePageNumber).getMediaBox();
            pdDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rect;
    }
}
