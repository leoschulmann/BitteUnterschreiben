package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.view.ThumbnailButton;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class GUIController {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());

    public static void openOption(AppWindow appWindow) {
        FileIOController.openPdfFile(appWindow);
    }

    public static void placeOption(JFrame appWindow) {
        String file = FilePicker.openOverlay(appWindow);
        if (file != null) {
            FileIOController.loadOverlay(new File(file));
            MainPanelController.repaint();
            DocumentController.setChanged(true);
        }
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

    public static ThumbnailButton generateThumbnailButton(BufferedImage thumbnail, Page p) {
        ThumbnailButton jb = new ThumbnailButton(thumbnail, p);
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
        if (mundaneCheck(bundle.getString("exit.message"))) {
            LoggerFactory.getLogger(GUIController.class).debug("Quitting");
            System.exit(0);
        }
    }

    public static void closeDocument() {
        if (mundaneCheck(bundle.getString("close.message"))) {
            LoggerFactory.getLogger(GUIController.class).debug("Closing document");
            while (DocumentController.getCurrentPage() != null) {
                DocumentController.deletePage(DocumentController.getCurrentPage());  //todo write simple method
            }
            DocumentController.setFilename(null);
            DocumentController.setChanged(false);
        }
    }

    private static boolean mundaneCheck(String message) {
        int answer = 0;
        if (DocumentController.isChanged()) {
            answer = JOptionPane.showConfirmDialog(BitteUnterschreiben.getApp(), message,
                    bundle.getString("message"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        }
        return answer == 0;  //JOptionPane 'OK' == 0, 'Cancel' == 2
    }
}
