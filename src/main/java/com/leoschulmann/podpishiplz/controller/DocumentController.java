package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.model.Document;
import com.leoschulmann.podpishiplz.model.Overlay;
import com.leoschulmann.podpishiplz.model.Page;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DocumentController {
    private static Document doc;

    private static Page currentPage;

    public static void setCurrentPage(Page currentPage) {
        DocumentController.currentPage = currentPage;
    }

    public static Page getCurrentPage() {
        return currentPage;
    }

    public static Document getDoc() {
        return doc;
    }

    public static void createDocument() {
        doc = new Document();
    }

    public static Page addNewPageToDocument(BufferedImage im, String file, int sourcePdfPage) {
        Page p = doc.addPageAndReturn(im, file, sourcePdfPage);
        currentPage = p;
        return p;
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

    public static void addNewOverlay(BufferedImage image) {
        Overlay o = new Overlay(image);
        o.setRelCentX(Math.random());
        o.setRelCentY(Math.random());
        o.setSelected(true);
        currentPage.getOverlays().forEach(overlay -> overlay.setSelected(false));
        currentPage.getOverlays().add(o);
    }

    public static void removeSelectedOverlay() {
        Overlay ov = currentPage.getOverlays().stream().filter(Overlay::isSelected).findFirst().orElse(null);
        currentPage.getOverlays().remove(ov);
    }
}
