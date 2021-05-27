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
    private static final double MINIMUM_ZOOM = 0.05d;
    private static final double MAXIMUM_ZOOM = 10.d;

    @Getter
    private static double zoom = 1.d;

    public static int getOverlayResizeWidth(Overlay o) {
        return (int) (o.getWidth() * zoom);
    }

    public static int getOverlayResizeHeight(Overlay o) {
        return (int) (o.getHeight() * zoom);
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
        return getInsetX() + (int) (getZoomedImageWidth() * o.getRelCentX()) - getOverlayResizeWidth(o) / 2;
    }

    public static int getOverlayY(Overlay o) {
        return getInsetY() + (int) (getZoomedImageHeight() * o.getRelCentY()) - getOverlayResizeHeight(o) / 2;
    }

    public static Optional<Rectangle> getSelectedOverlayBounds() {
        return getOverlays().stream().filter(Overlay::isSelected).findFirst().map(Overlay::getBounds);
    }

    public static void initListener() {
        EventListener el = (event, object) -> {
            switch (event) {
                case PAGE_ROTATED:
                    resetZoom();
                    redrawInPageMode();
                    break;
                case DRAG_ENTER_EVENT:
                    redrawInDraggingMode();
                    break;
                case DRAG_EXIT_EVENT:
                    redrawInEmptyMode();
                    break;
                case DROP_EVENT:
                    GUIController.openFile((String) object);
                    break;
                case OPEN_WORKER_FINISHED:
                    redrawInPageMode();
                    break;
            }
        };
        EventController.subscribe(EventType.PAGE_ROTATED, el);
        EventController.subscribe(EventType.DROP_EVENT, el);
        EventController.subscribe(EventType.DRAG_ENTER_EVENT, el);
        EventController.subscribe(EventType.DRAG_EXIT_EVENT, el);
    }

    static void resetZoom() {
        int vpW = mainPanel.getMainPanelWrapper().getWidth();
        int vpH = mainPanel.getMainPanelWrapper().getHeight();

        double widthRatio = 1. * getImage().getWidth() / vpW;
        double heightRatio = 1. * getImage().getHeight() / vpH;

        zoom = widthRatio > heightRatio ? 1. / (widthRatio * 1.1) : 1. / (heightRatio * 1.1);
        log.debug("setting init zoom value {}", zoom);
    }

    static void setPageCentered() {
        int vpW = mainPanel.getMainPanelWrapper().getViewport().getWidth();
        int vpH = mainPanel.getMainPanelWrapper().getViewport().getHeight();

        int x = (getPanelWidth() - vpW) / 2;
        int y = (getPanelHeight() - vpH) / 2;

        Point initCenter = new Point(Math.max(x, 0), Math.max(y, 0));

        mainPanel.getMainPanelWrapper().getViewport().setViewPosition(initCenter);

        log.debug("setting init center:{}:{}", initCenter.getX(), initCenter.getY());
    }

    public static int getZoomedImageHeight() {
        return (int) (getImage().getHeight() * zoom);
    }

    public static int getZoomedImageWidth() {
        return (int) (getImage().getWidth() * zoom);
    }

    public static int getPanelWidth() {
        return getZoomedImageWidth() * 2;
    }

    public static int getPanelHeight() {
        return getZoomedImageHeight() * 2;
    }

    public static int getInsetX() {
        return (getPanelWidth() - getZoomedImageWidth()) / 2;
    }

    public static int getInsetY() {
        return (getPanelHeight() - getZoomedImageHeight()) / 2;
    }

    public static void setPageX0(int pageX0) {
        if (pageX0 >= 0) MainPanelController.pageX0 = pageX0;
    }

    public static void setPageY0(int pageY0) {
        if (pageY0 >= 0) MainPanelController.pageY0 = pageY0;
    }

    public static void changeZoom(double zoomAmount) {
        double modifier = Math.pow(SettingsController.getZoomSpeed(),
                SettingsController.isInvertZoom() ? -zoomAmount : zoomAmount);

        if (zoom >= MINIMUM_ZOOM || modifier > 1.) {
            if (zoom <= MAXIMUM_ZOOM || modifier < 1.) {
                zoom *= modifier;
            }
        }
    }

    static void redrawInPageMode() {
        mainPanel.setMode(DrawMode.PAGE);
        mainPanel.repaint();

    }

    static void redrawInEmptyMode() {
        mainPanel.setMode(DrawMode.EMPTY);
        mainPanel.repaint();

    }

    private static void redrawInDraggingMode() {
        mainPanel.setMode(DrawMode.DND);
        mainPanel.repaint();
    }
}
