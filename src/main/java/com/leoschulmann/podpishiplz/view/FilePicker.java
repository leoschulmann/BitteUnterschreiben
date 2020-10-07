package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.EventListener;
import com.leoschulmann.podpishiplz.controller.EventType;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class FilePicker implements EventListener {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());


    public static String openPDF(JFrame parent) {
        FileDialog fileDialog = new FileDialog(parent, bundle.getString("open.file"), FileDialog.LOAD);
        fileDialog.setFile("*.pdf");   //for Win
        fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".pdf"));  // for Mac
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() == null) return null;
        return fileDialog.getDirectory() + fileDialog.getFile();
    }

    public static String savePDF(JFrame parent) {
        FileDialog fileDialog = new FileDialog(parent, bundle.getString("save.as"), FileDialog.SAVE);
        fileDialog.setFile("New PDF Document.pdf");
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        String filename;
        if (fileDialog.getFile() == null) {
            return null;
        } else {
            filename = fileDialog.getFile();
            if (!filename.endsWith(".pdf")) filename = filename + ".pdf";
            return fileDialog.getDirectory() + filename;
        }
    }

    public static String openOverlay(JFrame appWindow) {
        FileDialog fileDialog = new FileDialog(appWindow, bundle.getString("open.overlay"), FileDialog.LOAD);
        fileDialog.setFile("*.png");   //for Win
        fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".png"));  // for Mac
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() == null) return null;
        return fileDialog.getDirectory() + fileDialog.getFile();
    }

    @Override
    public void eventUpdate(EventType event, Object object) {
        if (event == EventType.LOCALE_CHANGED) {
            bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
        }
    }
}
