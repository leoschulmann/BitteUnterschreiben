package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.model.Document;
import com.leoschulmann.podpishiplz.model.Page;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DocumentController {
    private static Document doc;

    public static void createDocument() {
        doc = new Document();
    }

    public static int addNewPageToDocument(BufferedImage im, String file, int origPageNum) {
        return doc.addPage(im, file, origPageNum);
    }

    public static Page getPage(int page) throws IOException {
        Page p = doc.getPage(page);
        if (p.getImage() == null) {
            DocumentController.reloadFullScaleImage(page);
        }
        return doc.getPage(page);
    }

    public static void reloadFullScaleImage(int page) throws IOException {
        Page p = doc.getPage(page);
        p.setImage(PDFController.get300dpiPage(p.getOriginalFile(), p.getOriginalFilePageNumber()));
    }

    public static void removePages() {
    }

    public static void reorderPage() {
    }
}
