package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class MenuBar extends JMenuBar {
    private List<JMenuItem> optionList;   // future use   // wtf?
    private final JMenuItem optionOpen;
    private final JMenuItem optionPlace;
    private final JMenuItem optionRemove;
    private final JMenuItem optionSaveAs;
    private final JMenuItem optionSettings;

    public MenuBar() {
        JMenu menuFile = new JMenu("File");
        JMenu menuEdit = new JMenu("Edit");
        optionOpen = new JMenuItem("Open");
        optionPlace = new JMenuItem("Place");
        optionRemove = new JMenuItem("Remove selected");
        optionSaveAs = new JMenuItem("Save As...");
        optionSettings = new JMenuItem("Settings...");

        menuFile.add(optionOpen);
        menuFile.add(optionSaveAs);
        menuEdit.add(optionPlace);  // todo make disabled on empty window
        menuEdit.add(optionRemove);
        menuEdit.addSeparator();
        menuEdit.add(optionSettings);
        add(menuFile);
        add(menuEdit);
        optionOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        optionSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        optionPlace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        optionRemove.setAccelerator(KeyStroke.getKeyStroke("BACK_SPACE"));
        optionSettings.setAccelerator(KeyStroke.getKeyStroke("F12"));
    }

    public JMenuItem getOptionOpen() {
        return optionOpen;
    }

    public JMenuItem getOptionPlace() {
        return optionPlace;
    }

    public JMenuItem getOptionRemove() {
        return optionRemove;
    }

    public JMenuItem getOptionSaveAs() {
        return optionSaveAs;
    }

    public JMenuItem getOptionSettings() {
        return optionSettings;
    }
}
