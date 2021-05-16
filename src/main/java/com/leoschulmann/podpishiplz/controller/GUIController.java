package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.view.PageSelectorDialogue;
import com.leoschulmann.podpishiplz.view.PageSelectorElement;
import com.leoschulmann.podpishiplz.view.PageThumbnailButton;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class GUIController {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());

    static void openOption() {
        String file = FilePicker.openPDF(BitteUnterschreiben.getApp());
        openFile(file);
    }

    static void openFile(String file) {
        if (file != null) {
            closeDocument();
            FileIOController.openPdfFile(file, null);
            BitteUnterschreiben.getApp().setFrameTitle(file);
        }
    }

    static void addPagesFromFileOption() {
        String file = FilePicker.openPDF(BitteUnterschreiben.getApp());
        if (file != null) {
            PageSelectorElement[] previews;
            previews = FileIOController.loadPreviewsFromFile(file);
            PageSelectorDialogue psd = new PageSelectorDialogue(BitteUnterschreiben.getApp(), file, previews);
            boolean[] selectedPages = psd.showDialog();
            if (selectedPages != null) {
                LoggerFactory.getLogger(GUIController.class).debug("Selected pages: {}", (Object) selectedPages);
                FileIOController.openPdfFile(file, selectedPages);
                EventController.notify(EventType.FILE_MODIFIED, null);
            }
        }
    }

    static void placeOption() {
        String file = FilePicker.openOverlay(BitteUnterschreiben.getApp());
        if (file != null) {
            overlayThumbnailButtonOnClickAction(new File(file));
        }
    }

    static void overlayThumbnailButtonOnClickAction(File file) {
        FileIOController.loadOverlay(file);
        MainPanelController.setPageMode();
    }

    static void deleteSelectedOverlayOption() {
        DocumentController.removeSelectedOverlay();
        MainPanelController.setPageMode();
    }

    static void openPage(Page page) {
        if (page == null) {
            DocumentController.setCurrentPage(null);
            MainPanelController.setEmptyMode();
            EventController.notify(EventType.MAIN_PANEL_EMPTY, null);
        } else if (DocumentController.contains(page)) {
            DocumentController.setCurrentPage(page);
            MainPanelController.resetPosition();
            MainPanelController.setPageMode();
            EventController.notify(EventType.MAIN_PANEL_FULL, page);
            page.getOverlays().forEach(overlay -> overlay.setSelected(false));
            EventController.notify(EventType.OVERLAY_DESELECTED, null);
        }
    }

    static void saveFile() {
        FileIOController.blendAndSavePdfFile(SettingsController.getJpegQuality(),
                SettingsController.getBlender(), DocumentController.getFileName());
        EventController.notify(EventType.FILE_UNMODIFIED, null);
    }

    static void saveFileAs() {
        String file = FilePicker.savePdfDialog(BitteUnterschreiben.getApp());
        if (file != null) {
            FileIOController.blendAndSavePdfFile(SettingsController.getJpegQuality(),
                    SettingsController.getBlender(), file);
        }
    }

    static PageThumbnailButton generateThumbnailButton(BufferedImage thumbnail, Page p) {
        PageThumbnailButton jb = new PageThumbnailButton(thumbnail, p);
        jb.setVerticalTextPosition(SwingConstants.BOTTOM);
        jb.setHorizontalTextPosition(SwingConstants.CENTER);
        jb.addActionListener(e -> GUIController.openPage(jb.getPage()));
        TopPanelController.BUTTONS.add(jb);
        return jb;
    }

    static void placeButton(JButton jb) {
        TopPanelController.put(jb);
        TopPanelController.revalidateAndRepaint();
    }

    static void openSettingsDialogue() {
        SettingsController.openSettings();
    }

    static void quit() {
        if (mundaneCheckIfDocumentChanged(bundle.getString("exit.message"))) {
            LoggerFactory.getLogger(GUIController.class).debug("Quitting");
            DocumentController.purgeDocument();
            System.exit(0);
        }
    }

    static void closeDocument() {
        if (mundaneCheckIfDocumentChanged(bundle.getString("close.message"))) {
            LoggerFactory.getLogger(GUIController.class).debug("Closing document");
            DocumentController.purgeDocument();
            EventController.notify(EventType.FILE_UNMODIFIED, null);
            EventController.notify(EventType.NO_PAGES_IN_DOCUMENT, null);
            BitteUnterschreiben.getApp().resetFrameTitle();
        }
    }

    private static boolean mundaneCheckIfDocumentChanged(String message) {
        int answer = 0;
        if (DocumentController.isChanged()) {
            answer = JOptionPane.showConfirmDialog(BitteUnterschreiben.getApp(), message,
                    bundle.getString("message"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
        return answer == 0;  //JOptionPane 'OK' == 0, 'Cancel' == 2
    }

    public static void deleteAllOverlaysOption() {
        DocumentController.removeAllOverlaysFromPage();
        MainPanelController.setPageMode();
    }
}
