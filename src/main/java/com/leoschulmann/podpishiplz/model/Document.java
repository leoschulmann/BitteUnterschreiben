package com.leoschulmann.podpishiplz.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Document {
    private List<Page> pages;

    public Document() {
        pages = new ArrayList<>();
    }

    public List<BufferedImage> getThumbnails() {
        return pages.stream().map(Page::getThumbnail).collect(Collectors.toList());
    }

    public Page getPage(int pos) {
        return pages.get(pos);
    }

    public int addPage(BufferedImage th, String file, int origPageNum) {
        Page p = new Page(file, origPageNum);
        p.setThumbnail(th);
        pages.add(p);
        return pages.indexOf(p);
    }

    public void remPage(int pos) {
        Page p = pages.remove(pos);
        p.setThumbnail(null);
        p.setImage(null);
        p.setOverlays(null);  //might be excessive
    }
}
