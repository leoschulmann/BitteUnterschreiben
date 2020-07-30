package com.leoschulmann.podpishiplz.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    private static java.util.List<BufferedImage> overlays = new ArrayList<>();   //todo Map<Image, ImageCoords>
    private BufferedImage bufIm;
    private int resizeHeight = 0;
    private int resizeWidth = 0;
    private double resizeRatio = 0.0;
    private static final int INSET = 10;
    private int x1 = 0;
    private int x2 = 0;
    private int y1 = 0;
    private int y2 = 0;


    @Override
    protected void paintComponent(Graphics g) {
        if (bufIm != null) {
            if (bufIm.getHeight() > bufIm.getWidth()) { //picture is vertical
                resizeHeight = this.getHeight() - (INSET * 2);
                resizeWidth = resizeHeight * bufIm.getWidth() / bufIm.getHeight();
                x1 = (this.getWidth() - resizeWidth) / 2;
                y1 = INSET;
                x2 = x1 + resizeWidth;
                y2 = y1 + resizeHeight;

            } else { // horizontal
                resizeWidth = this.getWidth() - (INSET * 2);
                resizeHeight = resizeWidth * bufIm.getHeight() / bufIm.getWidth();

                x1 = INSET;
                y1 = (this.getHeight() - resizeHeight) / 2;
                x2 = x1 + resizeWidth;
                y2 = y1 + resizeHeight;
            }

            g.drawImage(bufIm, x1, y1, resizeWidth, resizeHeight, null);

            resizeRatio = 1.0 * resizeHeight / bufIm.getHeight();
//            System.out.println("Current scale:\t" + resizeRatio);
//            System.out.println("Page coords:\t[" + x1 + "," + y1 + "]\t[" + x2 + "," + y2 + "]");
        }
        if (overlays.size() > 0) {
            for (BufferedImage i : overlays) {
                g.drawImage(i, (x1 + x2) / 2, (y1 + y2) / 2,
                        (int) (i.getWidth() * resizeRatio), (int) (i.getHeight() * resizeRatio), null);
            }
        }
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufIm = bufferedImage;
    }

    public void addNewOverlay(String file) throws IOException {
        BufferedImage overlay = ImageIO.read(new File(file));
        overlays.add(overlay);
    }
}
