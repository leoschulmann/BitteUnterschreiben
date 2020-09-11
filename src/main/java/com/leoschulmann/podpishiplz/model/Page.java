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
    private List<Overlay> overlays;
    private int mediaWidth;  // in px @ 72 dpi
    private int mediaHeight;
    private BufferedImage renderedImage;


    public Page(String filename, int number) {
        overlays = new ArrayList<>();
        originalFile = new File(filename);
        originalFilePageNumber = number;
    }

    public BufferedImage getImage() {
        if (image == null) {
            image = PDFController.get300dpiPage(originalFile, originalFilePageNumber);
        }
        return image;
    }

    public List<Overlay> getOverlays() {
        return overlays;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }


    public void setOverlays(List<Overlay> overlays) {
        this.overlays = overlays;
    }

    public int getMediaWidth() {
        return mediaWidth;
    }

    public int getMediaHeight() {
        return mediaHeight;
    }

    public BufferedImage getRenderedImage() {
        return renderedImage;
    }

    public void setRenderedImage(BufferedImage renderedImage) {
        this.renderedImage = renderedImage;
    }

    public void setMediaWidth(int mediaWidth) {
        this.mediaWidth = mediaWidth;
    }

    public void setMediaHeight(int mediaHeight) {
        this.mediaHeight = mediaHeight;
    }
}
