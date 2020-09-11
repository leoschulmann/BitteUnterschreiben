package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.model.Page;

import javax.swing.*;

public class ThumbButtonContextMenu extends JPopupMenu {

    public ThumbButtonContextMenu(Page page) {

        JMenuItem toFront = new JMenuItem("Переместить в начало");
        JMenuItem left = new JMenuItem("Переместить левее");
        JMenuItem right = new JMenuItem("Переместить правее");
        JMenuItem toBack = new JMenuItem("Переместить в конец");
        JMenuItem delete = new JMenuItem("Удалить");

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

        add(toFront);
        add(left);
        add(right);
        add(toBack);
        addSeparator();
        add(delete);
    }
}
