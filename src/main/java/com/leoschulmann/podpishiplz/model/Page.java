package com.leoschulmann.podpishiplz.model;

import com.leoschulmann.podpishiplz.controller.PDFController;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Page {
    private final File originalFile;
    private final int originalFilePageNumber;
    private BufferedImage image;
    private BufferedImage thumbnail;
    private List<Overlay> overlays;
    private int mediaWidth;  // in px @ 72 dpi
    private int mediaHeight;
    private BufferedImage renderedImage;


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

    public BufferedImage getImage() {
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

    public int getMediaWidth() {  // todo refactor
        if (mediaWidth == 0) {
            mediaWidth = (int)(PDFController.getMediabox(originalFile, originalFilePageNumber).getWidth());
        }
        return mediaWidth;
    }

    public int getMediaHeight() {
        if (mediaHeight == 0) {
            mediaHeight = (int)(PDFController.getMediabox(originalFile, originalFilePageNumber).getHeight());
        }
        return mediaHeight;
    }

    public BufferedImage getRenderedImage() {
        return renderedImage;
    }

    public void setRenderedImage(BufferedImage renderedImage) {
        this.renderedImage = renderedImage;
    }
}
