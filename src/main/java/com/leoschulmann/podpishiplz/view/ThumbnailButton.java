package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.model.Page;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class ThumbnailButton extends JButton {
    private final Page page;
    private boolean marked = false;

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

    public void markLabel() {
        super.setText("[" + getText() + "]");
        marked = true;
    }

    public void unmarkLabel() {
        if (marked) {
            super.setText(getText().substring(1, getText().length() - 1));
            marked = false;
        }
    }

    @Override
    public void setText(String text) {
        if (marked) {
            super.setText("[" + text + "]");
        } else super.setText(text);
    }
}
