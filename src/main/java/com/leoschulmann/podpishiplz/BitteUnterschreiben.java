package com.leoschulmann.podpishiplz;

import com.leoschulmann.podpishiplz.controller.*;
import com.leoschulmann.podpishiplz.view.AppWindow;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class BitteUnterschreiben {
    static AppWindow app;

    public static void main(String[] asda) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> app = new AppWindow());
        init();
    }

    private static void init() {
        MainPanelController.setMainPanel(app.getMainPanel());
        TopPanelController.setTsp(app.getTopScrollerPanel());
        DocumentController.createDocument();
        EventController.notify(EventType.MAIN_PANEL_EMPTY, null);
        EventController.notify(EventType.NO_PAGES_IN_DOCUMENT, null);
    }

    public static AppWindow getApp() {
        return app;
    }
}
