package com.leoschulmann.podpishiplz.model;

import com.leoschulmann.podpishiplz.controller.PDFController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Page {
    private final File originalFile;
    private final int originalFilePageNumber;
    private BufferedImage image;
    private BufferedImage thumbnail;
    private List<Overlay> overlays;


    public Page(String filename, int number) {
        overlays = new ArrayList<>();
        originalFile = new File(filename);
        originalFilePageNumber = number;
    }

    public void addOverlay(BufferedImage im, double x, double y) {
        Overlay o = new Overlay(im);
        o.setRelCentX(x);
        o.setRelCentY(y);
        overlays.add(o);
    }

    public void remOverlay(Overlay o) {
        overlays.remove(o);
    }

    public File getOriginalFile() {
        return originalFile;
    }

    public BufferedImage getImage() throws IOException {
        if (image == null) {
            image = PDFController.get300dpiPage(originalFile, originalFilePageNumber);
        }
        return image;
    }

    public BufferedImage getThumbnail() {
        return thumbnail;
    }

    public List<Overlay> getOverlays() {
        return overlays;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setThumbnail(BufferedImage thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setOverlays(List<Overlay> overlays) {
        this.overlays = overlays;
    }

    public int getOriginalFilePageNumber() {
        return originalFilePageNumber;
    }
}
