package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderComposite;
import com.leoschulmann.podpishiplz.model.Document;
import com.leoschulmann.podpishiplz.model.Overlay;
import com.leoschulmann.podpishiplz.model.Page;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class DocumentController {
    private static Document doc;

    private static Page currentPage;

    public static void setCurrentPage(Page page) {
        DocumentController.currentPage = page;
    }

    public static Page getCurrentPage() {
        return currentPage;
    }

    public static void createDocument() {
        doc = new Document();
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

    public static void addFileToDocument(PDDocument pdDocument, String filename) {
        BufferedImage[] thumbnails = PDFController.generatePageThumbnails(pdDocument);
        PDRectangle[] mediaBoxes = PDFController.getMediaBoxes(pdDocument);

        for (int pg = 0; pg < pdDocument.getNumberOfPages(); pg++) {
            Page p = new Page(filename, pg);
            p.setMediaWidth((int) (mediaBoxes[pg].getWidth()));
            p.setMediaHeight((int) (mediaBoxes[pg].getHeight()));
            p.setThumbnail(thumbnails[pg]);
            addPage(p);
            if (getAllPages().size() == 1) {
                EventController.notify(EventType.PAGES_ADDED, null);
            }
            GUIController.placeButton(GUIController.generateThumbnailButton(thumbnails[pg], p));
        }
    }

    private static void addPage(Page p) {
        doc.getPages().add(p);
    }

    public static boolean contains(Page page) {
        return doc.getPages().contains(page);
    }

    public static List<Page> getAllPages() {
        return doc.getPages();
    }

    public static int getPageNumber(Page p) {
        if (!contains(p)) return -1;
        else return doc.getPages().indexOf(p);
    }
}
