package com.leoschulmann.podpishiplz.model;

import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventType;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Overlay {
    @Getter
    private final BufferedImage image;

    @Getter
    private boolean selected;

    @Setter
    @Getter
    private double relCentX;

    @Setter
    @Getter
    private double relCentY;

    @Getter
    private final Rectangle bounds;

    @Setter
    @Getter
    private double rotation;

    public Overlay(BufferedImage image) {
        this.image = image;
        this.bounds = new Rectangle();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (this.selected) {
            EventController.notify(EventType.OVERLAY_SELECTED, this);
            EventController.notify(EventType.FILE_MODIFIED, null);
        }
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public void setBounds(int x, int y, int width, int height) {
        this.bounds.setBounds(x, y, width, height);
    }
}
