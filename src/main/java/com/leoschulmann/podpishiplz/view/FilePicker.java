package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class FilePicker {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang");


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
}
