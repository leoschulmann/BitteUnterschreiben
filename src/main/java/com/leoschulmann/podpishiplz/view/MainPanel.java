package com.leoschulmann.podpishiplz.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private static final java.util.List<Overlay> overlays = new ArrayList<>();
    private BufferedImage bufIm;
    private int resizeHeight = 0;
    private int resizeWidth = 0;
    private double resizeRatio = 0.0;
    private static final int INSET = 10;
    private int x1 = 0;
    private int y1 = 0;

    public MainPanel() {
//        MouseController mouse = new MouseController(overlays);
//        addMouseListener(mouse);
//        addMouseMotionListener(mouse);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                overlays.forEach(overlay -> overlay.setSelected(false));
                overlays.stream().filter(o -> o.getBounds().contains(e.getX(), e.getY()))
                        .findFirst()
                        .ifPresent(o -> o.setSelected(true));
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bufIm != null) {
            if (bufIm.getHeight() > bufIm.getWidth()) { //picture is vertical
                resizeHeight = this.getHeight() - (INSET * 2);
                resizeWidth = resizeHeight * bufIm.getWidth() / bufIm.getHeight();
                x1 = (this.getWidth() - resizeWidth) / 2;
                y1 = INSET;

            } else { // horizontal
                resizeWidth = this.getWidth() - (INSET * 2);
                resizeHeight = resizeWidth * bufIm.getHeight() / bufIm.getWidth();
                x1 = INSET;
                y1 = (this.getHeight() - resizeHeight) / 2;
            }

            g.drawImage(bufIm, x1, y1, resizeWidth, resizeHeight, null);

            resizeRatio = 1.0 * resizeHeight / bufIm.getHeight();
        }

        if (overlays.size() > 0) {
            overlays.forEach(o -> {
                int overlayResizeWidth = (int) (o.getWidth() * resizeRatio);
                int overlayResizeHeight = (int) (o.getHeight() * resizeRatio);
                int overlayX = x1 + (int) (o.getRelCentX() * resizeWidth) - overlayResizeWidth / 2;
                int overlayY = y1 + (int) (o.getRelCentY() * resizeHeight) - overlayResizeHeight / 2;
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
}
