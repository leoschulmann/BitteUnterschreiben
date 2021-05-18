package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.model.Overlay;
import com.leoschulmann.podpishiplz.view.DrawMode;
import com.leoschulmann.podpishiplz.view.MainPanel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MainPanelController {
    @Setter
    private static MainPanel mainPanel;

    @Getter
    private static int pageX0;

    @Getter
    private static int pageY0;

    @Getter
    private static int pageHeight;

    @Getter
    private static int pageWidth;

    private static double imAspectRatio;

    @Setter
    @Getter
    private static boolean rotatingMode;

    private static final int INSET = 10;  // margin (px)
    private static final int MIN_ZOOM_SIZE = 10;

    public static int getOverlayResizeWidth(Overlay o) {
        return (int) (o.getWidth() * getResizeRatio());
    }

    public static int getOverlayResizeHeight(Overlay o) {
        return (int) (o.getHeight() * getResizeRatio());
    }

    // resized page size
    private static int getPageStartHeight() {
        log.debug("Panel AR : {}, image AR {}",
                getPanelAspectRatio(), imAspectRatio);
        if (getPanelAspectRatio() > imAspectRatio) {
            return mainPanel.getMainPanelWrapper().getHeight() - (INSET * 2);
        } else {
            return (int) (getPageStartWidth() / imAspectRatio);
        }
    }

    private static int getPageStartWidth() {
        if (imAspectRatio > getPanelAspectRatio()) {
            return mainPanel.getMainPanelWrapper().getWidth() - (INSET * 2);
        } else {
            return (int) (getPageStartHeight() * imAspectRatio);
        }
    }

    private static double getPanelAspectRatio() {
        return 1. * mainPanel.getMainPanelWrapper().getWidth() / mainPanel.getMainPanelWrapper().getHeight();
    }

    // resized page top left coords
    private static int getPageStartX() {
        if (getPanelAspectRatio() > imAspectRatio) {
            return (mainPanel.getMainPanelWrapper().getWidth() - getPageStartWidth()) / 2;
        } else return INSET;
    }

    private static int getPageStartY() {
        if (imAspectRatio > getPanelAspectRatio()) {
            return (mainPanel.getMainPanelWrapper().getHeight() - getPageStartHeight()) / 2;
        } else {
            return INSET;
        }
    }

    public static BufferedImage getImage() {
        return DocumentController.getCurrentPage().getImage();
    }

    public static List<Overlay> getOverlays() {
        return DocumentController.getCurrentPage().getOverlays();
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

    public static void initListener() {
        EventListener el = (event, object) -> {
            switch (event) {
                case PAGE_ROTATED:
                    resetPosition();
                    setPageMode();
                    break;
                case DRAG_ENTER_EVENT:
                    setDraggingMode();
                    break;
                case DRAG_EXIT_EVENT:
                    setEmptyMode();
                    break;
                case DROP_EVENT:
                    GUIController.openFile((String) object);
                    break;
                case OPEN_WORKER_FINISHED:
                    setPageMode();
                    break;
            }
        };
        EventController.subscribe(EventType.PAGE_ROTATED, el);
        EventController.subscribe(EventType.DROP_EVENT, el);
        EventController.subscribe(EventType.DRAG_ENTER_EVENT, el);
        EventController.subscribe(EventType.DRAG_EXIT_EVENT, el);
    }

    static void resetPosition() {
        imAspectRatio = 1. * getImage().getWidth() / getImage().getHeight();
        pageWidth = getPageStartWidth();
        pageHeight = getPageStartHeight();
        pageX0 = getPageStartX();
        pageY0 = getPageStartY();
        log.debug("Resetting page : size [{},{}], top left corner ({},{})", pageWidth, pageHeight, pageX0, pageY0);
    }

    public static void setPageX0(int pageX0) {
        if (pageX0 >= 0) MainPanelController.pageX0 = pageX0;
    }

    public static void setPageY0(int pageY0) {
        if (pageY0 >= 0) MainPanelController.pageY0 = pageY0;
    }

    public static void zoom(double zoomAmount) {
        double modifier = Math.pow(SettingsController.getZoomSpeed(),
                SettingsController.isInvertZoom() ? -zoomAmount : zoomAmount);
        if ((pageHeight >= MIN_ZOOM_SIZE && pageWidth >= MIN_ZOOM_SIZE) || modifier > 1.0) {
            pageHeight *= modifier;
            pageWidth = (int) (imAspectRatio * pageHeight);
        }
    }

    static void setPageMode() {
        mainPanel.setMode(DrawMode.PAGE);
        mainPanel.repaint();

    }

    static void setEmptyMode() {
        mainPanel.setMode(DrawMode.EMPTY);
        mainPanel.repaint();

    }

    private static void setDraggingMode() {
        mainPanel.setMode(DrawMode.DND);
        mainPanel.repaint();
    }
}
