package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventType;
import com.leoschulmann.podpishiplz.controller.MainPanelController;
import com.leoschulmann.podpishiplz.model.Overlay;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController extends MouseAdapter {

    private final MainPanel panel;
    private int clickX;
    private int clickY;

    public MouseController(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (DocumentController.getCurrentPage() != null && MainPanelController.getOverlays() != null) {
            clickX = e.getX();
            clickY = e.getY();
            MainPanelController.getOverlays().forEach(overlay -> overlay.setSelected(false));
            EventController.notify(EventType.OVERLAY_DESELECTED, null);
            MainPanelController.getOverlays().stream().filter(o -> o.getBounds().contains(e.getX(), e.getY()))
                    .findFirst()
                    .ifPresent(o -> o.setSelected(true));
            panel.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (DocumentController.getCurrentPage() != null && MainPanelController.getOverlays() != null) {
            MainPanelController.getOverlays().stream()
                    .filter(Overlay::isSelected)
                    .findFirst()
                    .ifPresent(overlay -> {
                        int shiftX = e.getX() - clickX;
                        int shiftY = e.getY() - clickY;
                        double mouseOffsetX = clickX - overlay.getBounds().getCenterX();
                        double mouseOffsetY = clickY - overlay.getBounds().getCenterY();
                        overlay.setRelCentX((1.0 * e.getX() - MainPanelController.getPageStartX() - mouseOffsetX) / MainPanelController.getPageWidth());
                        overlay.setRelCentY((1.0 * e.getY() - MainPanelController.getPageStartY() - mouseOffsetY) / MainPanelController.getPageHeight());
                        clickX += shiftX;
                        clickY += shiftY;
                        panel.repaint();
                    });
        }
    }
}
