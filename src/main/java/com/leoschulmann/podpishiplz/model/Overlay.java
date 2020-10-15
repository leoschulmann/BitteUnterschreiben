package com.leoschulmann.podpishiplz.model;

import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Overlay {
    private final BufferedImage image;
    private boolean selected;
    private double relCentX;
    private double relCentY;
    private final Rectangle bounds;
    private double rotation;


    public Overlay(BufferedImage image) {
        this.image = image;
        this.bounds = new Rectangle();
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (this.selected) {
            EventController.notify(EventType.OVERLAY_SELECTED, this);
        }
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public double getRelCentX() {
        return relCentX;
    }

    public void setRelCentX(double relCentX) {
        this.relCentX = relCentX;
    }

    public double getRelCentY() {
        return relCentY;
    }

    public void setRelCentY(double relCentY) {
        this.relCentY = relCentY;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.bounds.setBounds(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
