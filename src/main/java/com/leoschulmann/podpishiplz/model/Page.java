package com.leoschulmann.podpishiplz.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Page {
    private File originalFile;
    private int originalFilePageNumber;
    private BufferedImage image;
    private BufferedImage thumbnail;
    private Map<BufferedImage, Position> overlays;


    public Page(String filename, int number) {
        overlays = new HashMap<>();
        originalFile = new File(filename);
        originalFilePageNumber = number;
    }

    public void addOverlay(BufferedImage im, double x, double y) {
        overlays.put(im, new Position(x, y));
    }

    public void remOverlay() {
        //todo remove overlay
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getThumbnail() {
        return thumbnail;
    }

    public Map<BufferedImage, Position> getOverlays() {
        return overlays;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setThumbnail(BufferedImage thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setOverlays(Map<BufferedImage, Position> overlays) {
        this.overlays = overlays;
    }

    public int getOriginalFilePageNumber() {
        return originalFilePageNumber;
    }
}
