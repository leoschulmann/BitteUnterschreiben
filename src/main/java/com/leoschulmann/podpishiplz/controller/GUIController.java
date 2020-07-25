package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.view.AppWindow;
import com.leoschulmann.podpishiplz.view.FilePicker;
import com.leoschulmann.podpishiplz.view.TopScrollerPanel;

import java.io.IOException;

public class GUIController {
   public static void openOption(AppWindow appWindow, TopScrollerPanel topScrollerPanel){
        String file = FilePicker.open(appWindow);
        if (file != null) {
            try {
                topScrollerPanel.loadFile(file);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }
}
