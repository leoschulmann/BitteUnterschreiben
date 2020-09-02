package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.view.ThumbnailButton;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TopPanelController {
    private static TopScrollerPanel tsp;
    public static final List<ThumbnailButton> buttons = new ArrayList<>();


    public static TopScrollerPanel getTsp() {
        return tsp;
    }

    public static void setTsp(TopScrollerPanel tsp) {
        TopPanelController.tsp = tsp;
    }

    public static JScrollPane getWrapper() {
        return tsp.getWrapper();
    }

    public static void removeAll() {
        tsp.getPanel().removeAll();
    }

    public static void revalidateAndRepaint() {
        tsp.getPanel().revalidate();
        tsp.getPanel().repaint();

    }

    public static void put(JButton jb) {
        tsp.put(jb);
    }

    public static void remove(Component c) {
        tsp.rem(c);
    }

    public static List<ThumbnailButton> getButtons() {
        return buttons;
    }

    public static void clearAndPlaceThumbnailsOrdered() {
        buttons.stream().sorted((o1, o2) -> {
            if (DocumentController.getPageNumber(o1.getPage()) < DocumentController.getPageNumber(o2.getPage())) {
                return -1;
            } else return 1;
        }).forEach(b -> {
                    b.setText(String.valueOf(DocumentController.getPageNumber(b.getPage()) + 1));
                    GUIController.placeButton(b);
                }
        );
    }
}
