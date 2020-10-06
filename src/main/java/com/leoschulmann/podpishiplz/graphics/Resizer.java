package com.leoschulmann.podpishiplz.graphics;

import org.slf4j.LoggerFactory;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Resizer {
    static final AffineTransformOp halvingOp = new AffineTransformOp(
            AffineTransform.getScaleInstance(0.5, 0.5),
            AffineTransformOp.TYPE_BILINEAR);

    public static BufferedImage resize(BufferedImage im, int desiredHeight) {
        LoggerFactory.getLogger(Resizer.class).debug("Resizing image : {}px -> {}px", im.getHeight(), desiredHeight);
        while (getRatio(im.getHeight(), desiredHeight) < 0.5) {
            im = reduceImageInHalf(im);
        }

        double lastPassRatio = getRatio(im.getHeight(), desiredHeight);
        int desiredWidth = (int) (im.getWidth() * lastPassRatio);
        BufferedImage resultPicture = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_ARGB);
        AffineTransform transform = AffineTransform.getScaleInstance(lastPassRatio, lastPassRatio);
        AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        resultPicture = operation.filter(im, resultPicture);
        return resultPicture;
    }

    private static BufferedImage reduceImageInHalf(BufferedImage im) {
        BufferedImage output = new BufferedImage(im.getWidth() / 2, im.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
        return halvingOp.filter(im, output);
    }

    static double getRatio(int inputImageHeight, int outputImageHeight) {
        return 1.0 * outputImageHeight / inputImageHeight;
    }
}
