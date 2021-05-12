package com.leoschulmann.podpishiplz.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Document {
    @Getter
    private final List<Page> pages;

    public Document() {
        pages = new ArrayList<>();
    }

    public void remPage(int pos) {
        Page p = pages.remove(pos);
        p.setImage(null);
        p.setOverlays(null);  //might be excessive
    }
}
