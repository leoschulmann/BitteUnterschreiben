package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class MenuBar extends JMenuBar {
    private List<JMenuItem> optionList;   // future use
    private final JMenuItem optionOpen;

    public MenuBar() {
        JMenu menuFile = new JMenu("File");
        optionOpen = new JMenuItem("Open");
        menuFile.add(optionOpen);
        add(menuFile);
        optionOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
    }

    public JMenuItem getOptionOpen() {
        return optionOpen;
    }
}
