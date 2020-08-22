package com.leoschulmann.podpishiplz.graphics;

import java.awt.*;
import java.awt.image.ColorModel;
import java.lang.reflect.InvocationTargetException;

public class BlenderComposite implements Composite {
    public BlenderComposite(Class<? extends CompositeContext> contextClass) {
        this.contextClass = contextClass;
    }

    Class<? extends CompositeContext> contextClass;

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        CompositeContext compositeContext = null;
        try {
            compositeContext = contextClass
                    .getConstructor(ColorModel.class, ColorModel.class, RenderingHints.class)
                    .newInstance(srcColorModel, dstColorModel, hints);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return compositeContext;
    }
}
