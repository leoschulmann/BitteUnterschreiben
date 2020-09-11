package com.leoschulmann.podpishiplz.view;


import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.controller.MainPanelController;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel {

    public MainPanel() {
        MainPanelController.setMainPanel(this);
        MouseController mouse = new MouseController(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (DocumentController.getCurrentPage() != null) {

            int pageHeight = MainPanelController.getPageHeight();
            int pageWidth = MainPanelController.getPageWidth();
            int pageX0 = MainPanelController.getPageStartX();
            int pageY0 = MainPanelController.getPageStartY();

            g.drawImage(MainPanelController.getImage(), pageX0, pageY0, pageWidth, pageHeight, null);

            if (MainPanelController.getOverlays().size() > 0) {
                MainPanelController.getOverlays().forEach(o -> {
                    int overlayResizeWidth = MainPanelController.getOverlayResizeWidth(o);
                    int overlayResizeHeight = MainPanelController.getOverlayResizeHeight(o);
                    int overlayX = MainPanelController.getOverlayX(o);
                    int overlayY = MainPanelController.getOverlayY(o);
                    o.setBounds(overlayX, overlayY, overlayResizeWidth, overlayResizeHeight);
                    g.drawImage(o.getImage(), overlayX, overlayY, overlayResizeWidth, overlayResizeHeight, null);
                });
            }

            MainPanelController.getSelectedOverlayBounds().ifPresent(bounds -> {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.GREEN);
                g2.setStroke(new BasicStroke(2));
                g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

            });
        }
        else {
            BufferedImage im;
            try {
                im = ImageIO.read(this.getClass().getClassLoader().getResource("pholder.png"));
            } catch (Exception e) {
                LoggerFactory.getLogger(MainPanel.class).error(e.getMessage(), e);
                JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                im = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics gr = im.createGraphics();
                gr.setColor(Color.red);
                gr.drawString("Error", 5, 50);
                gr.dispose();
            }
            g.drawImage(im, (this.getWidth() - im.getWidth())/2, (this.getHeight()-im.getHeight())/2,
                    im.getWidth(), im.getHeight(), null);
        }
    }
}
