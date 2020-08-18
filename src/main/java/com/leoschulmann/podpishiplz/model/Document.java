package com.leoschulmann.podpishiplz.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Document {
    private final List<Page> pages;

    public Document() {
        pages = new ArrayList<>();
    }

    public List<BufferedImage> getThumbnails() {
        return pages.stream().map(Page::getThumbnail).collect(Collectors.toList());
    }

    public Page getPage(int pos) {
        return pages.get(pos);
    }

    public Page addPageAndReturn(BufferedImage th, String file, int sourcePdfPage) {
        Page p = new Page(file, sourcePdfPage);
        p.setThumbnail(th);
        pages.add(p);
        return p;
    }

    public void remPage(int pos) {
        Page p = pages.remove(pos);
        p.setThumbnail(null);
        p.setImage(null);
        p.setOverlays(null);  //might be excessive
    }

    public boolean contains(Page page) {
        return pages.contains(page);
    }

    public List<Page> getPages() {
        return pages;
    }
}
