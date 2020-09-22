package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderComposite;
import com.leoschulmann.podpishiplz.graphics.Rotater;
import com.leoschulmann.podpishiplz.model.Document;
import com.leoschulmann.podpishiplz.model.Overlay;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.ThumbButtonContextMenu;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.lang.String.valueOf;

public class DocumentController {
    private static Document doc;

    private static Page currentPage;

    public static void setCurrentPage(Page page) {
        if (page != null && !contains(page)) {
            throw new IllegalArgumentException();
        }
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
        LoggerFactory.getLogger(DocumentController.class).debug("Initiating rendering pages.");
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
        LoggerFactory.getLogger(DocumentController.class).info("Finished rendering pages.");
    }

    public static void addFileToDocument(PDDocument pdDocument, String filename) {
        BufferedImage[] thumbnails = PDFController.generatePageThumbnails(pdDocument);
        PDRectangle[] mediaBoxes = PDFController.getMediaBoxes(pdDocument);
        Page firstPage = null;
        for (int pg = 0; pg < pdDocument.getNumberOfPages(); pg++) {
            Page p = new Page(filename, pg);
            if (firstPage == null) firstPage = p;
            p.setMediaWidth((int) (mediaBoxes[pg].getWidth()));
            p.setMediaHeight((int) (mediaBoxes[pg].getHeight()));
            addPage(p);
            if (getAllPages().size() == 1) {
                EventController.notify(EventType.PAGES_ADDED, null);
            }
            JButton jb = GUIController.generateThumbnailButton(thumbnails[pg], p);
            jb.setText(valueOf(getPageNumber(p) + 1));

            jb.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {  //works on mac
                    if (e.isPopupTrigger()) {
                        new ThumbButtonContextMenu(p).show(e.getComponent(), e.getX(), e.getY());
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) { // todo test on win
                    if (e.isPopupTrigger()) {
                        new ThumbButtonContextMenu(p).show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            GUIController.placeButton(jb);
        }
        if (currentPage == null) GUIController.openPage(firstPage);
    }

    public static void addPage(Page p) {
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

    public static void movePageToFront(Page page) {
        getAllPages().remove(page);
        getAllPages().add(0, page);
        EventController.notify(EventType.PAGES_REORDERED, null);
    }

    public static void movePageLeft(Page page) {
        int idx = getPageNumber(page);
        getAllPages().remove(page);
        getAllPages().add(idx - 1, page);
        EventController.notify(EventType.PAGES_REORDERED, null);
    }

    public static void movePageRight(Page page) {
        int idx = getPageNumber(page);
        getAllPages().remove(page);
        getAllPages().add(idx + 1, page);
        EventController.notify(EventType.PAGES_REORDERED, null);
    }

    public static void movePageToBack(Page page) {
        int size = getAllPages().size();
        getAllPages().remove(page);
        getAllPages().add(size - 1, page);
        EventController.notify(EventType.PAGES_REORDERED, null);
    }

    public static void deletePage(Page page) {
        int idx = getPageNumber(page);
        doc.remPage(getPageNumber(page));
        EventController.notify(EventType.PAGES_REORDERED, page);
        if (getAllPages().size() == 0) {
            EventController.notify(EventType.NO_PAGES_IN_DOCUMENT, null);
        } else if (idx >= getAllPages().size()) {
            GUIController.openPage(getAllPages().get(getAllPages().size() - 1));  // get last
        } else GUIController.openPage(getAllPages().get(idx));
    }

    public static void rotateLeft(Page page, boolean toLeft) {
        BufferedImage image = page.getImage();
        BufferedImage rotated = Rotater.rotate(image, toLeft);
        page.setImage(rotated);
        EventController.notify(EventType.PAGE_ROTATED, page);
    }
}
