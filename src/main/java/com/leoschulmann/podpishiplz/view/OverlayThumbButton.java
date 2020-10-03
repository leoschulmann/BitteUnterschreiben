package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class OverlayThumbButton extends JButton {
    private final File file;

    public OverlayThumbButton(BufferedImage pic, String tooltip, File file) {
        super(new ImageIcon(pic));
        this.setToolTipText(tooltip);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
