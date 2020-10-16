package com.leoschulmann.podpishiplz.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Rotater {


    public static BufferedImage rotate(BufferedImage image, boolean toLeft) {

        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage dest = new BufferedImage(h, w, image.getType());

        if (toLeft) {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    dest.setRGB(y, w - x - 1, image.getRGB(x, y));
                }
            }
        } else {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    dest.setRGB(h - y-1, x, image.getRGB(x, y));
                }
            }
        }
        return dest;
    }

    public static BufferedImage freeRotate(BufferedImage im, double radians) {
        BufferedImage rotated = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        int anchorX = im.getWidth() / 2;
        int anchorY = im.getHeight() / 2;
        at.rotate(radians, anchorX, anchorY);
        g.setTransform(at);
        g.drawImage(im, 0, 0, null);
        g.dispose();
        return rotated;
    }
}
