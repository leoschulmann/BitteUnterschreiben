package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.*;

public class FilePicker {

    public static String open(JFrame parent) {
        FileDialog fileDialog = new FileDialog(parent, "Открыть файл", FileDialog.LOAD);
        fileDialog.setFile("*.pdf");
        fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".pdf"));
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        if (fileDialog.getFile() == null) return null;
        return fileDialog.getDirectory() + fileDialog.getFile();
    }

    static void save(JFrame parent) {
    }

}
