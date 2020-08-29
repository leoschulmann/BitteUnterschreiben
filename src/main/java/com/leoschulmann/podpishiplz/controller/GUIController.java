package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.graphics.BlenderDarken;
import com.leoschulmann.podpishiplz.graphics.BlenderMultiply;
import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.MainPanel;
import com.leoschulmann.podpishiplz.view.SettingsDialogue;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.String.valueOf;

public class GUIController {
    private static MainPanel mainPanel;
    private static TopScrollerPanel topScrollerPanel;
    private static SettingsDialogue settingsDialogue;
    private static final EventListener GUIControllerEventListener;
    private static AppWindow window;

    static {
        GUIControllerEventListener = new EventListener() {
            JButton welcomeBtn = new JButton("Открыть .pdf...");

            @Override
            public void eventUpdate(EventType event, Object object) {
                welcomeBtn.addActionListener(e -> openOption(window));
                switch (event) {
                    case NO_PAGES_IN_DOCUMENT:
                        placeButton(welcomeBtn);
                        break;
                    case PAGES_ADDED:
                        remButton(welcomeBtn);
                        break;
                }
            }
        };
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, GUIControllerEventListener);
        EventController.subscribe(EventType.PAGES_ADDED, GUIControllerEventListener);
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

    private static void openPage(Page page) {
        if (DocumentController.contains(page)) {
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

    public static JButton generateThumbnailButton(BufferedImage thumbnail, Page p) {
        JButton jb = new JButton(new ImageIcon(thumbnail));
        jb.setText(valueOf(DocumentController.getPageNumber(p) + 1));
        jb.setVerticalTextPosition(SwingConstants.BOTTOM);
        jb.setHorizontalTextPosition(SwingConstants.CENTER);
        jb.addActionListener(e -> GUIController.openPage(p));
        return jb;
    }

    public static void placeButton(JButton jb) {
        topScrollerPanel.put(jb);
        topScrollerPanel.getPanel().revalidate();
        topScrollerPanel.getPanel().repaint();
    }

    public static void remButton(JButton jb) {
        topScrollerPanel.rem(jb);
    }

    public static void openSettingsDialogue(AppWindow appWindow) {
        if (settingsDialogue == null) {
            settingsDialogue = new SettingsDialogue(appWindow);
        }
        settingsDialogue.setVisible(true);
    }
}
