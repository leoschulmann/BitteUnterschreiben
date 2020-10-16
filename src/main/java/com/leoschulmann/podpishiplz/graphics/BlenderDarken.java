package com.leoschulmann.podpishiplz.graphics;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class BlenderDarken implements  CompositeContext {
    private final ColorModel srcCM;
    private final ColorModel dstCM;

   public BlenderDarken(ColorModel srcCM, ColorModel dstCM, RenderingHints hints) {
        this.srcCM = srcCM;
        this.dstCM = dstCM;
    }

    @Override
    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
        int compWidth = Math.min(src.getWidth(), dstIn.getWidth());
        int compHeight = Math.min(src.getHeight(), dstIn.getHeight());
        int[] srcPixels = new int[compWidth];
        int[] dstPixels = new int[compWidth];
        int pixel;
        int pixelRow = 0;
        while (pixelRow < compHeight) {
            src.getDataElements(0, pixelRow, compWidth, 1, srcPixels);
            dstIn.getDataElements(0, pixelRow, compWidth, 1, dstPixels);
            pixel = 0;
            while (pixel < compWidth) {
                dstPixels[pixel] = blend(srcPixels[pixel], dstPixels[pixel]);
                pixel++;
            }

            dstOut.setDataElements(0, pixelRow, compWidth, 1, dstPixels);
            pixelRow++;
        }
    }

    private int blend(int srPx, int dsPx) {
        int r = calc(dstCM.getRed(dsPx), srcCM.getRed(srPx), srcCM.getAlpha(srPx));
        int g = calc(dstCM.getGreen(dsPx), srcCM.getGreen(srPx), srcCM.getAlpha(srPx));
        int b = calc(dstCM.getBlue(dsPx), srcCM.getBlue(srPx), srcCM.getAlpha(srPx));
        int a = Math.min(255, dstCM.getAlpha(dsPx) + srcCM.getAlpha(srPx));

        return (b) | (g << 8) | (r << 16) | (a << 24);
    }

    /*
    Result channel value (X3) for 'Darken' would be calculated as following:
    X3 = X1 - (min(X1,X2)) * A2/255;
    where
    X1 - background channel value;
    X2 - overlying channel value;
    A2 - overlying channel alpha-value;
    min(X1,X2) - original 'Darken' algorithm

    X3 = X1 @ overlying alpha-channel == 0;
    X3 = min(X1, X2) @ overlying alpha-channel == 255;
     */
    private int calc(int dst, int src, int srcA) {
        return dst - (dst - Math.min(dst, src)) * srcA / 255;
    }


    @Override
    public void dispose() {

    }
}
