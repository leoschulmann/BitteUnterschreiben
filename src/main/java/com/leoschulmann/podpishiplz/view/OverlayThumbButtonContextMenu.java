package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;

import javax.swing.*;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class OverlayThumbButtonContextMenu extends JPopupMenu {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());

    public OverlayThumbButtonContextMenu(File f) {
        if (!bundle.getLocale().getLanguage().equals(Locale.getDefault().getLanguage())) {
            bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
        }

        JMenuItem delete = new JMenuItem(bundle.getString("delete"));
        delete.addActionListener(e -> SettingsController.removeOverlayFromList(f));
        add(delete);
    }
}
