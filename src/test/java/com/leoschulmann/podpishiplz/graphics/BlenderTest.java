package com.leoschulmann.podpishiplz.graphics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.*;

class BlenderTest {

    static int[] purplePixel;
    static int[] yellowPixel;
    static int[] mask;
    static WritableRaster yellowRaster;
    static WritableRaster purpleRaster;
    static WritableRaster dst;
    static ColorModel defaultCM;

    @BeforeAll
    static void prepare() {
        purplePixel = new int[]{0xFFA468C5};
        yellowPixel = new int[]{0xFFE2AE0E};
        mask = new int[]{0xFF0000, 0xFF00, 0xFF, 0xFF000000};
        purpleRaster = Raster.createPackedRaster(
                new DataBufferInt(purplePixel, 1), 1, 1, 1, mask, null);
        yellowRaster = Raster.createPackedRaster(
                new DataBufferInt(yellowPixel, 1), 1, 1, 1, mask, null);
        defaultCM = ColorModel.getRGBdefault();
    }

    @BeforeEach
     void resetDestinationRaster() {
        dst = Raster.createPackedRaster(
                new DataBufferInt(new int[1], 1), 1, 1, 1, mask, null);
    }

    @Test
    public void testMultiply() {
        BlenderMultiply bl = new BlenderMultiply(defaultCM, defaultCM, null);
        bl.compose(purpleRaster, yellowRaster, dst);
        BufferedImage im = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        im.setData(dst);
        Assertions.assertEquals(new Color(145, 70, 10), new Color(im.getRGB(0, 0)));
    }

    @Test
    public void testDarken() {
        BlenderDarken bl = new BlenderDarken(defaultCM, defaultCM, null);
        bl.compose(purpleRaster, yellowRaster, dst);
        BufferedImage im = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        im.setData(dst);
        Assertions.assertEquals(new Color(164, 104, 14), new Color(im.getRGB(0, 0)));
    }
}