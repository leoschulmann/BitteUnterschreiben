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
}
