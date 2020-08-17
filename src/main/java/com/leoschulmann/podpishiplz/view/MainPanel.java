package com.leoschulmann.podpishiplz.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel {
    private final java.util.List<Overlay> overlays = new ArrayList<>();
    private BufferedImage bufIm;  // real (actual) page image
    private int pageHeight = 0;  // resized page size
    private int pageWidth = 0;
    private double resizeRatio = 0.0;  // resized divided by real
    private static final int INSET = 10;  // margin (px)
    private int pageX0 = 0;  // resized page top left coords
    private int pageY0 = 0;

    public MainPanel() {
        MouseController mouse = new MouseController(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bufIm != null) {
            if (bufIm.getHeight() > bufIm.getWidth()) { //picture is vertical
                pageHeight = this.getHeight() - (INSET * 2);
                pageWidth = pageHeight * bufIm.getWidth() / bufIm.getHeight();
                pageX0 = (this.getWidth() - pageWidth) / 2;
                pageY0 = INSET;

            } else { // horizontal
                pageWidth = this.getWidth() - (INSET * 2);
                pageHeight = pageWidth * bufIm.getHeight() / bufIm.getWidth();
                pageX0 = INSET;
                pageY0 = (this.getHeight() - pageHeight) / 2;
            }

            g.drawImage(bufIm, pageX0, pageY0, pageWidth, pageHeight, null);

            resizeRatio = 1.0 * pageHeight / bufIm.getHeight();
        }

        if (overlays.size() > 0) {
            overlays.forEach(o -> {
                int overlayResizeWidth = (int) (o.getWidth() * resizeRatio);
                int overlayResizeHeight = (int) (o.getHeight() * resizeRatio);
                int overlayX = pageX0 + (int) (o.getRelCentX() * pageWidth) - overlayResizeWidth / 2;
                int overlayY = pageY0 + (int) (o.getRelCentY() * pageHeight) - overlayResizeHeight / 2;
                o.setBounds(overlayX, overlayY, overlayResizeWidth, overlayResizeHeight);
                g.drawImage(o.getImage(), overlayX, overlayY, overlayResizeWidth, overlayResizeHeight, null);
            });
        }

        overlays.stream().filter(Overlay::isSelected)
                .findFirst()
                .ifPresent(o -> {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(Color.GREEN);
                    g2.setStroke(new BasicStroke(3));
                    g.drawRect(o.getBounds().x, o.getBounds().y, o.getBounds().width, o.getBounds().height);
                });
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufIm = bufferedImage;
    }

    public void addNewOverlay(String file) throws IOException {
        Overlay o = new Overlay(ImageIO.read(new File(file)));
        o.setRelCentX(Math.random());
        o.setRelCentY(Math.random());
        o.setSelected(true);
        overlays.forEach(overlay -> overlay.setSelected(false));
        overlays.add(o);
    }

    public void removeSelectedOverlay() {
        Overlay o = overlays.stream().filter(Overlay::isSelected).findFirst().orElse(null);
        overlays.remove(o);
        repaint();
    }

    public List<Overlay> getOverlays() {
        return overlays;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public int getPageX0() {
        return pageX0;
    }

    public int getPageY0() {
        return pageY0;
    }
}
