package com.leoschulmann.podpishiplz.model;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private final List<Page> pages;

    public Document() {
        pages = new ArrayList<>();
    }

    public void remPage(int pos) {
        Page p = pages.remove(pos);
        p.setImage(null);
        p.setOverlays(null);  //might be excessive
    }

    public List<Page> getPages() {
        return pages;
    }
}
