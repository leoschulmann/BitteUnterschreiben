package com.leoschulmann.podpishiplz.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Overlay {
    private final BufferedImage image;
    private boolean selected;
    private double relCentX;
    private double relCentY;
    private final Rectangle bounds;


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
}
