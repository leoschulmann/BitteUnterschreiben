package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.graphics.Resizer;
import com.leoschulmann.podpishiplz.model.Overlay;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.ThumbnailButton;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TopPanelController {
    private static TopScrollerPanel tsp;
    public static final List<ThumbnailButton> BUTTONS = new ArrayList<>();
    final static JButton welcomeBtn = new JButton("Открыть .pdf...");

    static {
        welcomeBtn.addActionListener(e -> GUIController.openOption(BitteUnterschreiben.getApp()));
    }

    public static void setTsp(TopScrollerPanel tsp) {
        TopPanelController.tsp = tsp;
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

    public static void clearAndPlaceThumbnailsOrdered() {
        BUTTONS.stream().sorted((o1, o2) -> {
            if (o1.equals(o2)) return 0;
            else if (DocumentController.getPageNumber(o1.getPage()) < DocumentController.getPageNumber(o2.getPage())) {
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
                    TopPanelController.BUTTONS.remove(
                            TopPanelController.BUTTONS.stream()
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
                case PAGE_ROTATED:
                    Page pageRotated = (Page) object;
                    ThumbnailButton butt = BUTTONS.stream()
                            .filter(b -> b.getPage() == pageRotated)
                            .findFirst()
                            .orElse(null);
                    butt.setThumbnailImage(Resizer.resize(pageRotated.getImage(), 75));
                    butt.repaint();
                    break;
                case MAIN_PANEL_FULL:
                    Page page = (Page) object;
                    BUTTONS.forEach(ThumbnailButton::unmarkLabel);
                    BUTTONS.stream().filter(b -> b.getPage() == page)
                            .findFirst().ifPresent(ThumbnailButton::markLabel);
            }
        };
        EventController.subscribe(EventType.PAGES_REORDERED, el);
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, el);
        EventController.subscribe(EventType.PAGES_ADDED, el);
        EventController.subscribe(EventType.PAGE_ROTATED, el);
        EventController.subscribe(EventType.MAIN_PANEL_FULL, el);
    }
}
