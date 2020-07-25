package com.leoschulmann.podpishiplz.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ConsoleAppLogic {
    public static void convert(String[] args) {
        String inputPDFPath = args[0];
        String outputFolder = args[1];
        String overlayImagePath = args[2];

        try {
            File inputFile = new File(inputPDFPath);
            PDDocument sourceDocument = PDDocument.load(inputFile);
            PDFRenderer renderer = new PDFRenderer(sourceDocument);

            BufferedImage overlayImage = ImageIO.read(new File(overlayImagePath));
            PDDocument resultPDFDocument = new PDDocument();

            for (int i = 0; i < sourceDocument.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                Graphics graphics = image.getGraphics();
                graphics.drawImage(overlayImage, image.getWidth() / 2, image.getHeight() / 2, null);
                graphics.dispose();

                PDPage page = new PDPage(PDRectangle.A4);
                resultPDFDocument.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(resultPDFDocument, page, PDPageContentStream.AppendMode.APPEND, true);
                PDImageXObject pdImageXObject = JPEGFactory.createFromImage(resultPDFDocument, image);
                contentStream.drawImage(pdImageXObject, 0, 0,
                        pdImageXObject.getWidth() / 300f * 72, pdImageXObject.getHeight() / 300f * 72);
                contentStream.close();
            }

            resultPDFDocument.save(new File(outputFolder + "signed_" + inputFile.getName()));

            sourceDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
