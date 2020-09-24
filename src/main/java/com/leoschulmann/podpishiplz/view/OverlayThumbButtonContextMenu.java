package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;

import javax.swing.*;
import java.io.File;

public class OverlayThumbButtonContextMenu extends JPopupMenu {
    public OverlayThumbButtonContextMenu(File f) {
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(e -> SettingsController.removeOverlayFromList(f));
        add(delete);
    }
}
