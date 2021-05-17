package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderComposite;
import com.leoschulmann.podpishiplz.graphics.Rotater;
import com.leoschulmann.podpishiplz.model.Document;
import com.leoschulmann.podpishiplz.model.Overlay;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.PageThumbButtonContextMenu;
import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.valueOf;

public class DocumentController {
    private static Document doc;
    @Getter
    @Setter
    private static String fileName;
    @Getter
    private static Page currentPage;
    @Getter
    private static boolean changed;

    public static void setCurrentPage(Page page) {
        if (page != null && !contains(page)) {
            throw new IllegalArgumentException();
        }
        DocumentController.currentPage = page;
    }

    public static void createDocument() {
        doc = new Document();
        changed = false;
    }

    static void addNewOverlay(BufferedImage image) {
        Overlay o = new Overlay(image);
        o.setRelCentX(Math.random());
        o.setRelCentY(Math.random());
        o.setSelected(true);
        currentPage.getOverlays().forEach(overlay -> overlay.setSelected(false));
        currentPage.getOverlays().add(o);
    }

    static void removeSelectedOverlay() {
        Overlay ov = currentPage.getOverlays().stream().filter(Overlay::isSelected).findFirst().orElse(null);
        currentPage.getOverlays().remove(ov);
    }

    public static void renderPage(Class<? extends CompositeContext> blender, Page p) {
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
            BufferedImage bim;
            if (o.getRotation() == 0.) {
                bim = o.getImage();
            } else {
                bim = Rotater.freeRotate(o.getImage(), o.getRotation());
            }

            g2d.drawImage(bim,
                    (int) (o.getRelCentX() * imWidth - (ovWidth / 2)),
                    (int) (o.getRelCentY() * imHeight - (ovHeight / 2)),
                    ovWidth, ovHeight, null);
        }
        g2d.dispose();
        p.setRenderedImage(render);
    }


    public static void addFileToDocument(PDDocument pdDocument, String filename, boolean[] selectedPages) {
        if (selectedPages == null) {  //simple 'Open' file yields null'ed array
            selectedPages = new boolean[pdDocument.getNumberOfPages()];
            Arrays.fill(selectedPages, true);
        }
        BufferedImage[] thumbnails = PDFController.generatePageThumbnails(pdDocument, selectedPages);
        PDRectangle[] mediaBoxes = PDFController.getMediaBoxes(pdDocument, selectedPages);
        Page firstPage = null;
        if (getFileName() == null) { //new document
            setFileName(filename);
        }
        for (int pg = 0; pg < pdDocument.getNumberOfPages(); pg++) {
            if (!selectedPages[pg]) continue;
            Page p = new Page(filename, pg);
            if (firstPage == null) firstPage = p;
            p.setMediaWidth((int) (mediaBoxes[pg].getWidth()));
            p.setMediaHeight((int) (mediaBoxes[pg].getHeight()));
            addPage(p);
            if (pg == 0) {
                EventController.notify(EventType.PAGES_ADDED, null);
            }
            JButton jb = GUIController.generateThumbnailButton(thumbnails[pg], p);
            jb.setText(valueOf(getPageNumber(p) + 1));

            jb.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {  //works on mac
                    if (e.isPopupTrigger()) {
                        new PageThumbButtonContextMenu(p).show(e.getComponent(), e.getX(), e.getY());
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) { // todo test on win
                    if (e.isPopupTrigger()) {
                        new PageThumbButtonContextMenu(p).show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            GUIController.placeButton(jb);
        }
        if (currentPage == null) GUIController.openPage(firstPage);
    }

    static void addPage(Page p) {
        doc.getPages().add(p);
    }

    static boolean contains(Page page) {
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
        int idx = getPageNumber(page);
        if (idx > 0) {
            getAllPages().remove(page);
            getAllPages().add(0, page);
            EventController.notify(EventType.PAGES_REORDERED, null);
            EventController.notify(EventType.FILE_MODIFIED, true);
        }
    }

    public static void movePageLeft(Page page) {
        int idx = getPageNumber(page);
        if (idx > 0) {
            getAllPages().remove(page);
            getAllPages().add(idx - 1, page);
            EventController.notify(EventType.PAGES_REORDERED, null);
            EventController.notify(EventType.FILE_MODIFIED, true);
        }
    }

    public static void movePageRight(Page page) {
        int idx = getPageNumber(page);
        if (idx < doc.getPages().size() - 1) {
            getAllPages().remove(page);
            getAllPages().add(idx + 1, page);
            EventController.notify(EventType.PAGES_REORDERED, null);
            EventController.notify(EventType.FILE_MODIFIED, true);
        }
    }

    public static void movePageToBack(Page page) {
        int idx = getPageNumber(page);
        if (idx < doc.getPages().size() - 1) {
            int size = getAllPages().size();
            getAllPages().remove(page);
            getAllPages().add(size - 1, page);
            EventController.notify(EventType.PAGES_REORDERED, null);
            EventController.notify(EventType.FILE_MODIFIED, true);
        }
    }

    public static void deletePage(Page page) {
        int idx = getPageNumber(page);
        remPage(getPageNumber(page));
        EventController.notify(EventType.PAGES_REORDERED, page);
        if (getAllPages().size() == 0) {
            EventController.notify(EventType.NO_PAGES_IN_DOCUMENT, null);
        } else if (idx >= getAllPages().size()) {
            GUIController.openPage(getAllPages().get(getAllPages().size() - 1));  // get last
            EventController.notify(EventType.FILE_MODIFIED, true);
        } else {
            GUIController.openPage(getAllPages().get(idx));
            EventController.notify(EventType.FILE_MODIFIED, true);
        }
    }

    public static void rotateLeft(Page page, boolean toLeft) {
        BufferedImage image = page.getImage();
        BufferedImage rotated = Rotater.rotate(image, toLeft);
        page.setImage(rotated);
        int temp = page.getMediaHeight();
        page.setMediaHeight(page.getMediaWidth());
        page.setMediaWidth(temp);
        EventController.notify(EventType.PAGE_ROTATED, page);
        EventController.notify(EventType.FILE_MODIFIED, true);
    }


    public static void initListener() {
        EventListener el = (event, object) -> {
            switch (event) {
                case FILE_MODIFIED:
                    changed = true;
                    break;
                case NO_PAGES_IN_DOCUMENT:
                    changed = false;
                    fileName = null;
                    break;
                case FILE_UNMODIFIED:
                    changed = false;
                    break;
            }
        };
        EventController.subscribe(EventType.FILE_MODIFIED, el);
        EventController.subscribe(EventType.FILE_UNMODIFIED, el);
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, el);
    }

    public static void removeAllOverlaysFromPage() {
        getCurrentPage().getOverlays().removeAll(getCurrentPage().getOverlays());
    }

    public static void purgeDocument() {
        getAllPages().forEach(p -> {
            p.getOverlays().clear();
            p.setImage(null);
            p.setRenderedImage(null);
        });
        getAllPages().clear();
        TopPanelController.removeAll();
        TopPanelController.revalidateAndRepaint();
        System.gc();
    }

    public static void remPage(int pos) {
        Page p = doc.getPages().remove(pos);
        p.setImage(null);
        p.setOverlays(null);
        p.setRenderedImage(null);
    }
}
