package com.leoschulmann.podpishiplz.view;


import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.controller.MainPanelController;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import com.leoschulmann.podpishiplz.graphics.Rotater;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel {
    private final JScrollPane mainPanelWrapper;

    public MainPanel() {
        mainPanelWrapper = new JScrollPane(this);
        mainPanelWrapper.setBorder(BorderFactory.createEmptyBorder());
        MainPanelController.setMainPanel(this);
        MouseController mouse = new MouseController(this);
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
        if (DocumentController.getCurrentPage() != null) {

            int pageHeight = MainPanelController.getPageHeight();
            int pageWidth = MainPanelController.getPageWidth();
            int pageX0 = MainPanelController.getPageX0();
            int pageY0 = MainPanelController.getPageY0();
            setPreferredSize(new Dimension(pageX0 + pageWidth, pageY0 + pageHeight));
            g.drawImage(MainPanelController.getImage(), pageX0, pageY0, pageWidth, pageHeight, null);

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
                g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

            });
        } else {
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
            int imX0 = (mainPanelWrapper.getWidth() - im.getWidth()) / 2;
            int imY0 = (mainPanelWrapper.getHeight() - im.getHeight()) / 2;

            g.drawImage(im, imX0, imY0, im.getWidth(), im.getHeight(), null);
            setPreferredSize(new Dimension(im.getWidth(), im.getHeight()));
        }
    }

    public JScrollPane getMainPanelWrapper() {
        return mainPanelWrapper;
    }
}
