package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.model.Page;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class PageThumbButtonContextMenu extends JPopupMenu {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());

    public PageThumbButtonContextMenu(Page page) {
        if (!bundle.getLocale().getLanguage().equals(Locale.getDefault().getLanguage())) {
            bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
        }


        JMenuItem toFront = new JMenuItem(bundle.getString("make.first"));
        JMenuItem left = new JMenuItem(bundle.getString("move.left"));
        JMenuItem right = new JMenuItem(bundle.getString("move.right"));
        JMenuItem toBack = new JMenuItem(bundle.getString("make.last"));
        JMenuItem rotLeft = new JMenuItem(bundle.getString("rotate.left"));
        JMenuItem rotRight = new JMenuItem(bundle.getString("rotate.right"));
        JMenuItem delete = new JMenuItem(bundle.getString("delete"));

        if (DocumentController.getPageNumber(page) == 0) {
            toFront.setEnabled(false);
            left.setEnabled(false);
        }

        if (DocumentController.getPageNumber(page) == DocumentController.getAllPages().size() - 1) {
            toBack.setEnabled(false);
            right.setEnabled(false);
        }
        toFront.addActionListener(e -> DocumentController.movePageToFront(page));
        left.addActionListener(e -> DocumentController.movePageLeft(page));
        right.addActionListener(e -> DocumentController.movePageRight(page));
        toBack.addActionListener(e -> DocumentController.movePageToBack(page));
        delete.addActionListener(e -> DocumentController.deletePage(page));
        rotLeft.addActionListener(e -> DocumentController.rotateLeft(page, true));
        rotRight.addActionListener(e -> DocumentController.rotateLeft(page, false));

        add(toFront);
        add(left);
        add(right);
        add(toBack);
        addSeparator();
        add(rotLeft);
        add(rotRight);
        addSeparator();
        add(delete);
    }
}
