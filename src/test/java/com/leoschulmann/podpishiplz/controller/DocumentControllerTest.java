package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.model.Page;
import com.leoschulmann.podpishiplz.view.MainPanel;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;

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

    @Test
    void openAndDeleteLast() {
        Page p1 = new Page("", 0);
        Page p2 = new Page("", 0);
        Page p3 = new Page("", 0);
        DocumentController.addPage(p1);
        DocumentController.addPage(p2);
        DocumentController.addPage(p3);
        DocumentController.setCurrentPage(p3);
        MainPanel mainPanelMock = Mockito.mock(MainPanel.class);
        Mockito.doNothing().when(mainPanelMock).repaint();
        MainPanelController.setMainPanel(mainPanelMock);
        DocumentController.deletePage(p3);
        Assertions.assertEquals(p2, DocumentController.getCurrentPage());
    }

    @Test
    void openAndDeleteMiddle() {
        Page p1 = new Page("", 0);
        Page p2 = new Page("", 0);
        Page p3 = new Page("", 0);
        DocumentController.addPage(p1);
        DocumentController.addPage(p2);
        DocumentController.addPage(p3);
        DocumentController.setCurrentPage(p2);
        MainPanel mainPanelMock = Mockito.mock(MainPanel.class);
        Mockito.doNothing().when(mainPanelMock).repaint();
        MainPanelController.setMainPanel(mainPanelMock);
        DocumentController.deletePage(p2);
        Assertions.assertEquals(p3, DocumentController.getCurrentPage());
    }

    @Test
    void openAndDeleteSingle() {
        MainPanel mainPanelMock = Mockito.mock(MainPanel.class);
        Mockito.doNothing().when(mainPanelMock).repaint();
        MainPanelController.setMainPanel(mainPanelMock);
        GUIController.initListener();
        JPanel panelmock = Mockito.mock(JPanel.class);
        Mockito.doNothing().when(panelmock).removeAll();
        TopScrollerPanel tspmock = Mockito.mock(TopScrollerPanel.class);
        Mockito.when(tspmock.getPanel()).thenReturn(panelmock);
        TopPanelController.setTsp(tspmock);
        Page p = new Page("", 0);
        DocumentController.addPage(p);
        DocumentController.setCurrentPage(p);
        DocumentController.deletePage(p);
        Assertions.assertNull(DocumentController.getCurrentPage());
    }
}