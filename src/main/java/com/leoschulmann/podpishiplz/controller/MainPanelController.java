package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.model.Overlay;
import com.leoschulmann.podpishiplz.view.MainPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

public class MainPanelController {
    private static MainPanel panel;
    private static final int INSET = 10;  // margin (px)

    public static int getOverlayResizeWidth(Overlay o) {
        return (int) (o.getWidth() * getResizeRatio());
    }

    public static int getOverlayResizeHeight(Overlay o) {
        return (int) (o.getHeight() * getResizeRatio());
    }

    // resized page size
    public static int getPageHeight() {
        if (isVertical()) {
            return panel.getHeight() - (INSET * 2);
        } else {
            return getPageWidth() * getImage().getHeight() / getImage().getWidth();
        }
    }

    public static int getPageWidth() {
        if (isVertical()) {
            return getPageHeight() * getImage().getWidth() / getImage().getHeight();
        } else {
            return panel.getWidth() - (INSET * 2);
        }
    }

    // resized page top left coords
    public static int getPageStartX() {
        if (isVertical()) {
            return (panel.getWidth() - getPageWidth()) / 2;
        } else return INSET;
    }

    public static int getPageStartY() {
        if (isVertical()) {
            return INSET;
        } else {
            return (panel.getHeight() - getPageHeight()) / 2;
        }
    }

    public static BufferedImage getImage() {
        return DocumentController.getCurrentPage().getImage();
    }

    public static List<Overlay> getOverlays() {
        if (DocumentController.getCurrentPage() == null) return null;
        return DocumentController.getCurrentPage().getOverlays();
    }

    private static boolean isVertical() {
        return getImage().getHeight() > getImage().getWidth();
    }

    // resized divided by real
    public static double getResizeRatio() {
        return 1.0 * getPageHeight() / getImage().getHeight();
    }

    public static int getOverlayX(Overlay o) {
        return getPageStartX() + (int) (o.getRelCentX() * getPageWidth()) - getOverlayResizeWidth(o) / 2;
    }

    public static int getOverlayY(Overlay o) {
        return getPageStartY() + (int) (o.getRelCentY() * getPageHeight()) - getOverlayResizeHeight(o) / 2;
    }

    public static Optional<Rectangle> getSelectedOverlayBounds() {
        return getOverlays().stream().filter(Overlay::isSelected).findFirst().map(Overlay::getBounds);
    }

    public static void setMainPanel(MainPanel mainpanel) {
        panel = mainpanel;
    }

    public static MainPanel getMainPanel() {
        return panel;
    }

    public static void repaint() {
        panel.repaint();
    }
}
