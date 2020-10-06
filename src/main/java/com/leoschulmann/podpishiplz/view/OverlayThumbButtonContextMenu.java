package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;

import javax.swing.*;
import java.io.File;
import java.util.ResourceBundle;

public class OverlayThumbButtonContextMenu extends JPopupMenu {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang");

    public OverlayThumbButtonContextMenu(File f) {
        JMenuItem delete = new JMenuItem(bundle.getString("delete"));
        delete.addActionListener(e -> SettingsController.removeOverlayFromList(f));
        add(delete);
    }
}
