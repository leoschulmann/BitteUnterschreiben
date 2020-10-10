package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.view.PageSelectorDialogue;
import com.leoschulmann.podpishiplz.view.PageThumbnailButton;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class GUIController {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());

    public static void openOption() {
        String file = FilePicker.openPDF(BitteUnterschreiben.getApp());
        if (file != null) {
            closeDocument();
            FileIOController.openPdfFile(file, null);
        }
    }

    public static void addPagesFromFileOption() {
        String file = FilePicker.openPDF(BitteUnterschreiben.getApp());
        if (file != null) {
            PageSelectorDialogue psd = new PageSelectorDialogue(BitteUnterschreiben.getApp(), file);
            boolean[] selectedPages = psd.showDialog();
            if (selectedPages != null) {
                LoggerFactory.getLogger(GUIController.class).debug("Selected pages: {}", (Object) selectedPages);
                FileIOController.openPdfFile(file, selectedPages);
                EventController.notify(EventType.FILE_MODIFIED, null);
            }
        }
    }

    public static void placeOption(JFrame appWindow) {
        String file = FilePicker.openOverlay(appWindow);
        if (file != null) {
            overlayThumbnailButtonOnClickAction(new File(file));
        }
    }

    public static void overlayThumbnailButtonOnClickAction(File file) {
        FileIOController.loadOverlay(file);
        MainPanelController.repaint();
        EventController.notify(EventType.FILE_MODIFIED, null);
    }

    public static void deleteSelectedOverlayOption() {
        DocumentController.removeSelectedOverlay();
        MainPanelController.repaint();
    }

    public static void openPage(Page page) {
        if (page == null) {
            DocumentController.setCurrentPage(null);
            MainPanelController.repaint();
            EventController.notify(EventType.MAIN_PANEL_EMPTY, null);
        } else if (DocumentController.contains(page)) {
            DocumentController.setCurrentPage(page);
            MainPanelController.resetPosition();
            MainPanelController.repaint();
            EventController.notify(EventType.MAIN_PANEL_FULL, page);
            page.getOverlays().forEach(overlay -> overlay.setSelected(false));
            EventController.notify(EventType.OVERLAY_DESELECTED, null);
        }
    }

    public static void saveFile() {
        FileIOController.blendAndSavePdfFile(SettingsController.getJpegQuality(),
                SettingsController.getBlender(), DocumentController.getFileName());
    }

    public static void saveFileAs() {
        String file = FilePicker.savePdfDialog(BitteUnterschreiben.getApp());
        if (file != null) {
            FileIOController.blendAndSavePdfFile(SettingsController.getJpegQuality(),
                    SettingsController.getBlender(), file);
        }
    }

    public static PageThumbnailButton generateThumbnailButton(BufferedImage thumbnail, Page p) {
        PageThumbnailButton jb = new PageThumbnailButton(thumbnail, p);
        jb.setVerticalTextPosition(SwingConstants.BOTTOM);
        jb.setHorizontalTextPosition(SwingConstants.CENTER);
        jb.addActionListener(e -> GUIController.openPage(jb.getPage()));
        TopPanelController.BUTTONS.add(jb);
        return jb;
    }

    public static void placeButton(JButton jb) {
        TopPanelController.put(jb);
        TopPanelController.revalidateAndRepaint();
    }

    public static void openSettingsDialogue() {
        SettingsController.openSettings();
    }

    public static void quit() {
        if (mundaneCheckIfDocumentChanged(bundle.getString("exit.message"))) {
            LoggerFactory.getLogger(GUIController.class).debug("Quitting");
            System.exit(0);
        }
    }

    public static void closeDocument() {
        if (mundaneCheckIfDocumentChanged(bundle.getString("close.message"))) {
            LoggerFactory.getLogger(GUIController.class).debug("Closing document");
            while (DocumentController.getCurrentPage() != null) {
                DocumentController.deletePage(DocumentController.getCurrentPage());  //todo write simple method
            }
            EventController.notify(EventType.FILE_UNMODIFIED, null);
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
}
