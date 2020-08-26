package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderComposite;
import com.leoschulmann.podpishiplz.model.Document;
import com.leoschulmann.podpishiplz.model.Overlay;
import com.leoschulmann.podpishiplz.model.Page;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    public static Page getPage(int page) {
        Page p = doc.getPage(page);
        if (p.getImage() == null) {
            DocumentController.reloadFullScaleImage(page);
        }
        return doc.getPage(page);
    }

    public static void reloadFullScaleImage(int page) {
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

    public static void renderAllPages(Class<? extends CompositeContext> blender) {  //todo add proper interpolation
        for (Page p : doc.getPages()) {
            BufferedImage im = p.getImage();
            int imWidth = (int) (im.getWidth() * SettingsController.getResolutionMultiplier());
            int imHeight = (int) (im.getHeight() * SettingsController.getResolutionMultiplier());
            BufferedImage render = new BufferedImage(imWidth, imHeight, im.getType());
            Graphics2D g2d = render.createGraphics();
            g2d.drawImage(im, 0, 0, imWidth, imHeight, null);
            if (blender != null) g2d.setComposite(new BlenderComposite(blender));
            for (Overlay o : p.getOverlays()) {
                int ovWidth = (int) (o.getImage().getWidth() * SettingsController.getResolutionMultiplier());
                int ovHeight = (int) (o.getImage().getHeight() * SettingsController.getResolutionMultiplier());
                g2d.drawImage(o.getImage(),
                        (int) (o.getRelCentX() * imWidth - (ovWidth / 2)),
                        (int) (o.getRelCentY() * imHeight - (ovHeight / 2)),
                        ovWidth, ovHeight, null);
            }
            g2d.dispose();
            p.setRenderedImage(render);
        }
    }
}
