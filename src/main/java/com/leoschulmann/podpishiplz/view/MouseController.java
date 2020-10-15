package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventType;
import com.leoschulmann.podpishiplz.controller.MainPanelController;
import com.leoschulmann.podpishiplz.model.Overlay;
import org.slf4j.LoggerFactory;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseController extends MouseAdapter {

    private final MainPanel panel;
    private int clickX;
    private int clickY;
    private final static int SHIFT_CHECK = InputEvent.SHIFT_DOWN_MASK | InputEvent.BUTTON1_DOWN_MASK;  // 1088
    private double startingAngle;

    public MouseController(MainPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (DocumentController.getCurrentPage() != null && MainPanelController.getOverlays() != null) {
            clickX = e.getX();
            clickY = e.getY();

            //on click+shift
            if (e.getModifiersEx() == SHIFT_CHECK) {
                MainPanelController.getOverlays().stream().filter(o -> o.getBounds().contains(e.getX(), e.getY()))
                        .findFirst()
                        .ifPresent(o -> {
                            o.setSelected(true);
                            startingAngle = getAngle( //image real center XY vs click XY
                                    o.getRelCentX() * MainPanelController.getPageWidth() + MainPanelController.getPageX0(),
                                    o.getRelCentY() * MainPanelController.getPageHeight() + MainPanelController.getPageY0(),
                                    clickX, clickY);
                        });
            } else {
                //on click
                MainPanelController.getOverlays().forEach(overlay -> overlay.setSelected(false));
                EventController.notify(EventType.OVERLAY_DESELECTED, null);
                MainPanelController.getOverlays().stream().filter(o -> o.getBounds().contains(e.getX(), e.getY()))
                        .findFirst()
                        .ifPresent(o -> o.setSelected(true));
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (DocumentController.getCurrentPage() != null && MainPanelController.getOverlays() != null) {
            MainPanelController.getOverlays().stream()
                    .filter(o -> o.getBounds().contains(e.getX(), e.getY()))
                    .findFirst()
                    .ifPresentOrElse(overlay -> {  //drag selected overlay on page
                                int shiftX = e.getX() - clickX;
                                int shiftY = e.getY() - clickY;
                                double mouseOffsetX = clickX - overlay.getBounds().getCenterX();
                                double mouseOffsetY = clickY - overlay.getBounds().getCenterY();
                                overlay.setRelCentX((1.0 * e.getX() - MainPanelController.getPageX0() - mouseOffsetX) / MainPanelController.getPageWidth());
                                overlay.setRelCentY((1.0 * e.getY() - MainPanelController.getPageY0() - mouseOffsetY) / MainPanelController.getPageHeight());
                                clickX += shiftX;
                                clickY += shiftY;
                                panel.repaint();
                            },
                            () -> {
                                if ((e.getModifiersEx() == SHIFT_CHECK)) {  // on shift and empty space rotate overlay
                                    MainPanelController.getOverlays().stream()
                                            .filter(Overlay::isSelected)
                                            .findFirst()
                                            .ifPresent(overlay -> {
                                                double rotateAngle = getAngle(
                                                        overlay.getRelCentX() * MainPanelController.getPageWidth() +
                                                                MainPanelController.getPageX0(),
                                                        overlay.getRelCentY() * MainPanelController.getPageHeight() +
                                                                MainPanelController.getPageY0(), e.getX(), e.getY())
                                                        - startingAngle;
                                                overlay.setRotation(rotateAngle);
                                                LoggerFactory.getLogger(MouseController.class).debug("Rotating {} deg",
                                                        (int) (Math.toDegrees(rotateAngle)));
                                                panel.repaint();
                                            });
                                } else {
                                    // drag the whole page on panel
                                    int deltax = e.getX() - clickX;
                                    int deltay = e.getY() - clickY;
                                    MainPanelController.setPageX0(MainPanelController.getPageX0() + deltax);
                                    MainPanelController.setPageY0(MainPanelController.getPageY0() + deltay);
                                    panel.repaint();
                                    clickX += deltax;
                                    clickY += deltay;
                                    panel.getMainPanelWrapper().setViewportView(panel);
                                }
                            });
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (DocumentController.getCurrentPage() != null && MainPanelController.getOverlays() != null) {
            MainPanelController.zoom(e.getPreciseWheelRotation());
            panel.getMainPanelWrapper().setViewportView(panel);
            panel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        panel.repaint();
    }

    private static double getAngle(double imX, double imY, int clckX, int clckY) {
        double angle = Math.atan2(clckX - imX, imY - clckY);
        if (angle < 0) {
            angle += Math.PI * 2;
        }
        return angle;
    }
}
