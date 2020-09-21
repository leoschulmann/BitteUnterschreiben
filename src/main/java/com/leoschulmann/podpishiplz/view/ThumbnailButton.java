package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.model.Page;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class ThumbnailButton extends JButton {
    private final Page page;

    public ThumbnailButton(BufferedImage thumbnail, Page page) {
        super(new ImageIcon(thumbnail));
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public void setThumbnailImage(BufferedImage image) {
        this.setIcon(new ImageIcon(image));
    }
}
