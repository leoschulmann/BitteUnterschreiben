package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.*;

public class FilePicker {

    public static String openPDF(JFrame parent) {
        FileDialog fileDialog = new FileDialog(parent, "Открыть файл", FileDialog.LOAD);
        fileDialog.setFile("*.pdf");   //for Win
        fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".pdf"));  // for Mac
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() == null) return null;
        return fileDialog.getDirectory() + fileDialog.getFile();
    }

    public static String savePDF(JFrame parent) {
        FileDialog fileDialog = new FileDialog(parent, "Сохранить как...", FileDialog.SAVE);
        fileDialog.setFile("*.pdf");   //for Win
        fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".pdf"));  // for Mac
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() == null) {
            return null;
        } else return fileDialog.getDirectory() + fileDialog.getFile();
    }

    public static String openOverlay(JFrame appWindow) {
        FileDialog fileDialog = new FileDialog(appWindow, "Открыть файл", FileDialog.LOAD);
        fileDialog.setFile("*.png");   //for Win
        fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".png"));  // for Mac
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() == null) return null;
        return fileDialog.getDirectory() + fileDialog.getFile();
    }
}
