package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderDarken;
import com.leoschulmann.podpishiplz.graphics.BlenderMultiply;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GUIController {
    private static MainPanel mainPanel;
    private static TopScrollerPanel topScrollerPanel;
    private static SettingsDialogue settingsDialogue;
    private static final EventListener GUIControllerEventListener;
    private static AppWindow window;
    public static final List<ThumbnailButton> buttons = new ArrayList<>();

    static {
        GUIControllerEventListener = new EventListener() {
            JButton welcomeBtn = new JButton("Открыть .pdf...");

            @Override
            public void eventUpdate(EventType event, Object object) {
                welcomeBtn.addActionListener(e -> openOption(window));
                switch (event) {
                    case NO_PAGES_IN_DOCUMENT:
                        placeButton(welcomeBtn);
                        openPage(null);
                        break;
                    case PAGES_ADDED:
                        remButton(welcomeBtn);
                        break;
                    case PAGES_REORDERED:
                        Page pageRemoved = (Page) object;
                        buttons.remove(buttons.stream()
                                .filter(b -> b.getPage() == pageRemoved)
                                .findFirst()
                                .orElse(null));

                        clearAndPlaceThumbnailsOrdered();
                        break;
                }
            }
        };
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, GUIControllerEventListener);
        EventController.subscribe(EventType.PAGES_ADDED, GUIControllerEventListener);
        EventController.subscribe(EventType.PAGES_REORDERED, GUIControllerEventListener);
    }

    public static void setAppWindow(AppWindow window) {
        GUIController.window = window;
    }

    public static void openOption(AppWindow appWindow) {
        FileIOController.openPdfFile(appWindow);
    }

    public static void placeOption(JFrame appWindow) {
        FileIOController.loadOverlay(appWindow);
        mainPanel.repaint();
    }

    public static void deleteSelectedOverlayOption() {
        DocumentController.removeSelectedOverlay();
        mainPanel.repaint();
    }

    public static void setMainPanel(MainPanel mainPanel) {
        GUIController.mainPanel = mainPanel;
    }

    public static void openPage(Page page) {
        if (page == null) {
            DocumentController.setCurrentPage(null);
            mainPanel.repaint();
            EventController.notify(EventType.MAIN_PANEL_EMPTY, null);
        } else if (DocumentController.contains(page)) {
            DocumentController.setCurrentPage(page);
            mainPanel.repaint();
            EventController.notify(EventType.MAIN_PANEL_FULL, null);
            page.getOverlays().forEach(overlay -> overlay.setSelected(false));
            EventController.notify(EventType.OVERLAY_DESELECTED, null);
        }
    }

    public static void saveFile(JFrame appWindow) {
        Class<? extends CompositeContext> blender = null;
        switch (SettingsController.getBlendingMode()) {
            case (1):
                blender = BlenderDarken.class;
                break;
            case (2):
                blender = BlenderMultiply.class;
                break;
            default:
        }
        FileIOController.blendAndSavePdfFile(appWindow, SettingsController.getJpegQuality(), blender);
    }

    public static void setTopScrollerPanel(TopScrollerPanel topScrollerPanel) {
        GUIController.topScrollerPanel = topScrollerPanel;
    }

    public static ThumbnailButton generateThumbnailButton(BufferedImage thumbnail, Page p) {
        ThumbnailButton jb = new ThumbnailButton(thumbnail, p);
        jb.setVerticalTextPosition(SwingConstants.BOTTOM);
        jb.setHorizontalTextPosition(SwingConstants.CENTER);
        jb.addActionListener(e -> GUIController.openPage(jb.getPage()));
        buttons.add(jb);
        return jb;
    }

    public static void clearAndPlaceThumbnailsOrdered() {
        topScrollerPanel.getPanel().removeAll();
        buttons.stream().sorted((o1, o2) -> {
            if (DocumentController.getPageNumber(o1.getPage()) < DocumentController.getPageNumber(o2.getPage())) {
                return -1;
            } else return 1;
        }).forEach(b -> {
                    b.setText(String.valueOf(DocumentController.getPageNumber(b.getPage()) + 1));
                    GUIController.placeButton(b);
                }
        );
        topScrollerPanel.getPanel().revalidate();
        topScrollerPanel.getPanel().repaint();
    }

    public static void placeButton(JButton jb) {
        topScrollerPanel.put(jb);
        topScrollerPanel.getPanel().revalidate();
        topScrollerPanel.getPanel().repaint();
    }

    public static void remButton(JButton jb) {
        topScrollerPanel.getPanel().remove(jb);
        topScrollerPanel.getPanel().revalidate();
        topScrollerPanel.getPanel().repaint();
    }

    public static void openSettingsDialogue(AppWindow appWindow) {
        if (settingsDialogue == null) {
            settingsDialogue = new SettingsDialogue(appWindow);
        }
        settingsDialogue.setVisible(true);
    }
}
