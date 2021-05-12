package com.leoschulmann.podpishiplz.view;


import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.controller.MainPanelController;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import com.leoschulmann.podpishiplz.graphics.Rotater;
import lombok.Getter;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel {
    @Getter
    private final JScrollPane mainPanelWrapper;
    private final Color bgColor;
    private final MouseController mouse;

    public MainPanel() {
        mainPanelWrapper = new JScrollPane(this);
        mainPanelWrapper.setBorder(BorderFactory.createEmptyBorder());
        bgColor = UIManager.getColor("Panel.background");
        MainPanelController.setMainPanel(this);
        mouse = new MouseController(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getMouseListeners().length == 0) addMouseListener(mouse);
        if (getMouseMotionListeners().length == 0) addMouseMotionListener(mouse);
        if (getMouseWheelListeners().length == 0) addMouseWheelListener(mouse);
        if (DocumentController.getCurrentPage() != null) {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            int pageHeight = MainPanelController.getPageHeight();
            int pageWidth = MainPanelController.getPageWidth();
            int pageX0 = MainPanelController.getPageX0();
            int pageY0 = MainPanelController.getPageY0();
            setPreferredSize(new Dimension(pageX0 + pageWidth, pageY0 + pageHeight));
            g.drawImage(MainPanelController.getImage(), pageX0, pageY0, pageWidth, pageHeight, null);
            g.setColor(Color.GRAY);
            g.drawRect(pageX0, pageY0, pageWidth, pageHeight);

            if (MainPanelController.getOverlays().size() > 0) {
                MainPanelController.getOverlays().forEach(o -> {
                    BufferedImage image;
                    int overlayResizeWidth;
                    int overlayResizeHeight;
                    int overlayX = MainPanelController.getOverlayX(o);
                    int overlayY = MainPanelController.getOverlayY(o);
                    if (o.getRotation() != 0.) {
                        image = Rotater.freeRotate(o.getImage(), o.getRotation());
                        overlayResizeWidth = (int) (MainPanelController.getResizeRatio() * image.getWidth());
                        overlayResizeHeight = (int) (MainPanelController.getResizeRatio() * image.getHeight());

                    } else {
                        image = o.getImage();
                        overlayResizeWidth = MainPanelController.getOverlayResizeWidth(o);
                        overlayResizeHeight = MainPanelController.getOverlayResizeHeight(o);
                    }
                    o.setBounds(overlayX, overlayY, overlayResizeWidth, overlayResizeHeight);
                    g.drawImage(image, overlayX, overlayY, overlayResizeWidth, overlayResizeHeight, null);
                });
            }

            MainPanelController.getSelectedOverlayBounds().ifPresent(bounds -> {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.decode('#' + SettingsController.getSelectionColor()));
                g2.setStroke(new BasicStroke(2));
                if (MainPanelController.isRotatingMode()) {
                    g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
                } else {
                    g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
                }

            });
        } else {
            removeMouseListener(mouse);
            removeMouseMotionListener(mouse);
            removeMouseWheelListener(mouse);
            BufferedImage im;
            try {
                im = ImageIO.read(this.getClass().getClassLoader().getResource("splash.png"));
            } catch (Exception e) {
                LoggerFactory.getLogger(MainPanel.class).error(e.getMessage(), e);
                JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                im = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics gr = im.createGraphics();
                gr.setColor(Color.red);
                gr.drawString("Error", 5, 50);
                gr.dispose();
            }
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());

            int imX0 = (mainPanelWrapper.getWidth() - im.getWidth()) / 2;
            int imY0 = (mainPanelWrapper.getHeight() - im.getHeight()) / 2;

            g.drawImage(im, imX0, imY0, im.getWidth(), im.getHeight(), null);
            setPreferredSize(new Dimension(im.getWidth(), im.getHeight()));
        }
    }
}
