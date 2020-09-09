package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.ThumbnailButton;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TopPanelController {
    private static TopScrollerPanel tsp;
    public static final List<ThumbnailButton> buttons = new ArrayList<>();
    final static JButton welcomeBtn = new JButton("Открыть .pdf...");

    static {
        welcomeBtn.addActionListener(e -> GUIController.openOption(BitteUnterschreiben.getApp()));
    }

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

    public static void initListener() {
        EventListener el = (event, object) -> {
            switch (event) {
                case PAGES_REORDERED:
                    Page pageRemoved = (Page) object;
                    getButtons().remove(
                            getButtons().stream()
                                    .filter(b -> b.getPage() == pageRemoved)
                                    .findFirst()
                                    .orElse(null));
                    removeAll();
                    clearAndPlaceThumbnailsOrdered();
                    revalidateAndRepaint();
                    break;
                case NO_PAGES_IN_DOCUMENT:
                    TopPanelController.put(welcomeBtn);
                    TopPanelController.revalidateAndRepaint();
                    GUIController.openPage(null);
                    break;
                case PAGES_ADDED:
                    TopPanelController.remove(welcomeBtn);
                    TopPanelController.revalidateAndRepaint();
                    break;
            }
        };
        EventController.subscribe(EventType.PAGES_REORDERED, el);
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, el);
        EventController.subscribe(EventType.PAGES_ADDED, el);
    }
}
