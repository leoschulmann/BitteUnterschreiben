package com.leoschulmann.podpishiplz.graphics;

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
}
