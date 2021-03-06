package com.leoschulmann.podpishiplz.graphics;


import lombok.extern.slf4j.Slf4j;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

@Slf4j
public class Resizer {
    private static final AffineTransformOp halvingOp = new AffineTransformOp(
            AffineTransform.getScaleInstance(0.5, 0.5),
            AffineTransformOp.TYPE_BILINEAR);

    public static BufferedImage resize(BufferedImage im, int desiredHeight, int maxWidth) {
        int desiredWidth;
        double pictureAR = getRatio(im.getWidth(), im.getHeight());

        if (pictureAR > getRatio(maxWidth, desiredHeight)) {
            desiredWidth = maxWidth;
            desiredHeight = (int) (1. * desiredWidth / pictureAR);
        } else {
            desiredWidth = (int) (1. * desiredHeight * pictureAR);
        }

        log.debug("Resizing image : {}x{}px -> {}x{}px", im.getWidth(), im.getHeight(), desiredWidth, desiredHeight);

        while (getRatio(im.getHeight(), desiredHeight) >= 2) {
            im = reduceImageInHalf(im);
        }

        double lastPassRatio = getRatio(desiredHeight, im.getHeight());

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

    private static double getRatio(int lengthA, int lengthB) {
        return 1.0 * lengthA / lengthB;
    }
}
