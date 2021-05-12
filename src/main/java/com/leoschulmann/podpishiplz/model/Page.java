package com.leoschulmann.podpishiplz.model;

import com.leoschulmann.podpishiplz.controller.PDFController;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Page {
    private final File originalFile;
    private final int originalFilePageNumber;

    @Setter
    private BufferedImage image;

    @Getter
    @Setter
    private List<Overlay> overlays;

    @Setter
    @Getter
    private int mediaWidth;  // in px @ 72 dpi

    @Setter
    @Getter
    private int mediaHeight;

    @Setter
    @Getter
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
}
