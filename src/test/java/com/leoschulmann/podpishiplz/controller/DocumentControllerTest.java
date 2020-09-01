package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.model.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentControllerTest {

    @BeforeEach
    void prepare() {
        DocumentController.createDocument();
    }

    @Test
    void setCurrentPage() {
        Page goodPage = new Page("", 0);
        Page badPage = new Page("", 0);
        DocumentController.addPage(goodPage);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DocumentController.setCurrentPage(badPage));
    }

    @Test
    void movePageToFront() {
        Page p1 = new Page("", 0);
        Page p2 = new Page("", 0);
        Page p3 = new Page("", 0);
        DocumentController.addPage(p1);
        DocumentController.addPage(p2);
        DocumentController.addPage(p3);
        DocumentController.movePageToFront(p3);
        Assertions.assertEquals(0, DocumentController.getPageNumber(p3));
    }

    @Test
    void movePageLeft() {
        Page p1 = new Page("", 0);
        Page p2 = new Page("", 0);
        Page p3 = new Page("", 0);
        DocumentController.addPage(p1);
        DocumentController.addPage(p2);
        DocumentController.addPage(p3);
        DocumentController.movePageLeft(p3);
        Assertions.assertEquals(1, DocumentController.getPageNumber(p3));

    }

    @Test
    void movePageRight() {
        Page p1 = new Page("", 0);
        Page p2 = new Page("", 0);
        Page p3 = new Page("", 0);
        DocumentController.addPage(p1);
        DocumentController.addPage(p2);
        DocumentController.addPage(p3);
        DocumentController.movePageRight(p1);
        Assertions.assertEquals(1, DocumentController.getPageNumber(p1));
    }

    @Test
    void movePageToBack() {
        Page p1 = new Page("", 0);
        Page p2 = new Page("", 0);
        Page p3 = new Page("", 0);
        DocumentController.addPage(p1);
        DocumentController.addPage(p2);
        DocumentController.addPage(p3);
        DocumentController.movePageToBack(p1);
        Assertions.assertEquals(2, DocumentController.getPageNumber(p1));

    }

    @Test
    void deletePage() {
        Page page = new Page("", 0);
        DocumentController.addPage(page);
        DocumentController.deletePage(page);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DocumentController.setCurrentPage(page));
    }

}