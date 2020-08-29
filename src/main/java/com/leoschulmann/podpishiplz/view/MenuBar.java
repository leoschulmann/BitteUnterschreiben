package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventListener;
import com.leoschulmann.podpishiplz.controller.EventType;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MenuBar extends JMenuBar implements EventListener {
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
        menuEdit.add(optionPlace);
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
        EventController.subscribe(EventType.MAIN_PANEL_EMPTY, this);
        EventController.subscribe(EventType.MAIN_PANEL_FULL, this);
        EventController.subscribe(EventType.PAGES_ADDED, this);
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, this);
        EventController.subscribe(EventType.OVERLAY_DESELECTED, this);
        EventController.subscribe(EventType.OVERLAY_SELECTED, this);
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

    @Override
    public void eventUpdate(EventType event, Object object) {
        switch (event) {
            case MAIN_PANEL_FULL:
                optionPlace.setEnabled(true);
                break;
            case MAIN_PANEL_EMPTY:
                optionPlace.setEnabled(false);
                optionRemove.setEnabled(false);
                break;
            case PAGES_ADDED:
                optionSaveAs.setEnabled(true);
                break;
            case NO_PAGES_IN_DOCUMENT:
                optionSaveAs.setEnabled(false);
                optionPlace.setEnabled(false);
                optionRemove.setEnabled(false);
                break;
            case OVERLAY_SELECTED:
                optionRemove.setEnabled(true);
                break;
            case OVERLAY_DESELECTED:
                optionRemove.setEnabled(false);
                break;
        }
    }
}
