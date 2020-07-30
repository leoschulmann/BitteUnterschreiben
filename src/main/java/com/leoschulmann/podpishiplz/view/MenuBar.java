package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class MenuBar extends JMenuBar {
    private List<JMenuItem> optionList;   // future use   // wtf?
    private final JMenuItem optionOpen;
    private final JMenuItem optionPlace;

    public MenuBar() {
        JMenu menuFile = new JMenu("File");
        JMenu menuEdit = new JMenu("Edit");
        optionOpen = new JMenuItem("Open");
        optionPlace = new JMenuItem("Place");

        menuFile.add(optionOpen);
        menuEdit.add(optionPlace);  // todo make disabled on empty window
        add(menuFile);
        add(menuEdit);
        optionOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        optionPlace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
    }

    public JMenuItem getOptionOpen() {
        return optionOpen;
    }

    public JMenuItem getOptionPlace() {
        return optionPlace;
    }
}
