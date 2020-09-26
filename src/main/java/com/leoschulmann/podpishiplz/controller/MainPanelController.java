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
    private static int pageX0;
    private static int pageY0;

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
    private static int getPageStartX() {
        if (isVertical()) {
            return (panel.getWidth() - getPageWidth()) / 2;
        } else return INSET;
    }

    private static int getPageStartY() {
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
        return getPageX0() + (int) (o.getRelCentX() * getPageWidth()) - getOverlayResizeWidth(o) / 2;
    }

    public static int getOverlayY(Overlay o) {
        return getPageY0() + (int) (o.getRelCentY() * getPageHeight()) - getOverlayResizeHeight(o) / 2;
    }

    public static Optional<Rectangle> getSelectedOverlayBounds() {
        return getOverlays().stream().filter(Overlay::isSelected).findFirst().map(Overlay::getBounds);
    }

    public static void setMainPanel(MainPanel mainpanel) {
        panel = mainpanel;
    }

    public static void repaint() {
        panel.repaint();
    }

    public static void initListener() {
        EventListener el = (event, object) -> {
            if (event == EventType.PAGE_ROTATED) {
                panel.repaint();
            }
        };
        EventController.subscribe(EventType.PAGE_ROTATED, el);
    }

    public static void resetPosition() {
        pageX0 = getPageStartX();
        pageY0 = getPageStartY();
    }

    public static int getPageX0() {
        return pageX0;
    }

    public static void setPageX0(int pageX0) {
        MainPanelController.pageX0 = pageX0;
    }

    public static int getPageY0() {
        return pageY0;
    }

    public static void setPageY0(int pageY0) {
        MainPanelController.pageY0 = pageY0;
    }
}
