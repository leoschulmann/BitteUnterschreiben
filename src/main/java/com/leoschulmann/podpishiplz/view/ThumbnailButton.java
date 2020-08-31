package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.model.Page;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class ThumbnailButton extends JButton {
    private BufferedImage thumbnail;
    private final Page page;

    public ThumbnailButton(BufferedImage thumbnail, Page page) {
        super(new ImageIcon(thumbnail));
        this.thumbnail = thumbnail;
        this.page = page;
    }

    public Page getPage() {
        return page;
    }
}
