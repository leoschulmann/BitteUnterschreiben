package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventListener;
import com.leoschulmann.podpishiplz.controller.EventType;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class MenuBar extends JMenuBar implements EventListener {
    private final JMenuItem optionOpen;
    private final JMenuItem optionPlace;
    private final JMenuItem optionRemove;
    private final JMenuItem optionSave;
    private final JMenuItem optionClose;
    private final JMenuItem optionExit;
    private final JMenuItem optionSaveAs;
    private final JMenuItem optionSettings;
    private final JMenu menuEdit;
    private final JMenu menuFile;
    private final JMenu pageSubmenu;
    private final JMenu overlaySubmenu;
    private final JMenuItem optionAddPages;
    private final JMenuItem optionRemovePage;
    private final JMenuItem optionPageToFront;
    private final JMenuItem optionPageToLeft;
    private final JMenuItem optionPageToRight;
    private final JMenuItem optionPageToBack;
    private final JMenuItem optionRotLeft;
    private final JMenuItem optionRotRight;
    private final JMenuItem optionRemAllOverlays;


    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
    private static Set<JMenuItem> pageOptions;
    private static Set<JMenuItem> overlayOptions;

    public MenuBar() {
        menuFile = new JMenu(bundle.getString("file"));
        menuEdit = new JMenu(bundle.getString("edit"));
        optionOpen = new JMenuItem(bundle.getString("open"));
        optionSave = new JMenuItem(bundle.getString("save"));
        optionSaveAs = new JMenuItem(bundle.getString("save.as"));
        optionClose = new JMenuItem(bundle.getString("close.document"));
        optionExit = new JMenuItem(bundle.getString("quit"));
        optionSettings = new JMenuItem(bundle.getString("settings"));
        pageSubmenu = new JMenu(bundle.getString("page"));
        overlaySubmenu = new JMenu(bundle.getString("overlay"));
        overlayOptions = new HashSet<>();
        overlayOptions.add(optionPlace = new JMenuItem(bundle.getString("place")));
        overlayOptions.add(optionRemove = new JMenuItem(bundle.getString("remove.selected")));
        overlayOptions.add(optionRemAllOverlays = new JMenuItem(bundle.getString("remove.all.overlays")));

        pageOptions = new HashSet<>();
        pageOptions.add(optionAddPages = new JMenuItem(bundle.getString("add.pages")));
        pageOptions.add(optionRemovePage = new JMenuItem(bundle.getString("delete")));
        pageOptions.add(optionPageToFront = new JMenuItem(bundle.getString("make.first")));
        pageOptions.add(optionPageToLeft = new JMenuItem(bundle.getString("move.left")));
        pageOptions.add(optionPageToRight = new JMenuItem(bundle.getString("move.right")));
        pageOptions.add(optionPageToBack = new JMenuItem(bundle.getString("make.last")));
        pageOptions.add(optionRotLeft = new JMenuItem(bundle.getString("rotate.left")));
        pageOptions.add(optionRotRight = new JMenuItem(bundle.getString("rotate.right")));


        menuFile.add(optionOpen);
        menuFile.add(optionSave);
        menuFile.add(optionSaveAs);
        menuFile.addSeparator();
        menuFile.add(optionClose);
        menuFile.add(optionExit);

        menuEdit.add(pageSubmenu);
        menuEdit.add(overlaySubmenu);

        overlaySubmenu.add(optionPlace);
        overlaySubmenu.add(optionRemove);
        overlaySubmenu.add(optionRemAllOverlays);

        pageSubmenu.add(optionAddPages);
        pageSubmenu.addSeparator();
        pageSubmenu.add(optionPageToFront);
        pageSubmenu.add(optionPageToLeft);
        pageSubmenu.add(optionPageToRight);
        pageSubmenu.add(optionPageToBack);
        pageSubmenu.addSeparator();
        pageSubmenu.add(optionRotLeft);
        pageSubmenu.add(optionRotRight);
        pageSubmenu.addSeparator();
        pageSubmenu.add(optionRemovePage);

        menuEdit.addSeparator();
        menuEdit.add(optionSettings);
        add(menuFile);
        add(menuEdit);
        optionOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        optionSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        optionSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        optionPlace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        optionRemove.setAccelerator(KeyStroke.getKeyStroke("BACK_SPACE"));
        optionSettings.setAccelerator(KeyStroke.getKeyStroke("F12"));
        EventController.subscribe(EventType.MAIN_PANEL_EMPTY, this);
        EventController.subscribe(EventType.MAIN_PANEL_FULL, this);
        EventController.subscribe(EventType.PAGES_ADDED, this);
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, this);
        EventController.subscribe(EventType.OVERLAY_DESELECTED, this);
        EventController.subscribe(EventType.OVERLAY_SELECTED, this);
        EventController.subscribe(EventType.LOCALE_CHANGED, this);
        EventController.subscribe(EventType.FILE_MODIFIED, this);
        EventController.subscribe(EventType.FILE_UNMODIFIED, this);
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

    public JMenuItem getOptionSave() {
        return optionSave;
    }

    public JMenuItem getOptionClose() {
        return optionClose;
    }

    public JMenuItem getOptionExit() {
        return optionExit;
    }

    public JMenuItem getOptionAddPages() {
        return optionAddPages;
    }

    public JMenuItem getOptionRemovePage() {
        return optionRemovePage;
    }

    public JMenuItem getOptionPageToFront() {
        return optionPageToFront;
    }

    public JMenuItem getOptionPageToLeft() {
        return optionPageToLeft;
    }

    public JMenuItem getOptionPageToRight() {
        return optionPageToRight;
    }

    public JMenuItem getOptionPageToBack() {
        return optionPageToBack;
    }

    public JMenuItem getOptionRotLeft() {
        return optionRotLeft;
    }

    public JMenuItem getOptionRotRight() {
        return optionRotRight;
    }

    public JMenuItem getOptionRemAllOverlays() {
        return optionRemAllOverlays;
    }

    @Override
    public void eventUpdate(EventType event, Object object) {
        switch (event) {
            case MAIN_PANEL_FULL:
                optionPlace.setEnabled(true);
                optionRemAllOverlays.setEnabled(true);
                break;
            case MAIN_PANEL_EMPTY:
                overlayOptions.forEach(e -> e.setEnabled(false));
                pageOptions.forEach(e -> e.setEnabled(false));
                break;
            case PAGES_ADDED:
                optionClose.setEnabled(true);
                pageOptions.forEach(e -> e.setEnabled(true));
                break;
            case NO_PAGES_IN_DOCUMENT:
                optionSaveAs.setEnabled(false);
                optionSave.setEnabled(false);
                optionClose.setEnabled(false);
                optionClose.setEnabled(false);
                overlayOptions.forEach(e -> e.setEnabled(false));
                pageOptions.forEach(e -> e.setEnabled(false));
                break;
            case OVERLAY_SELECTED:
                optionRemove.setEnabled(true);
                break;
            case OVERLAY_DESELECTED:
                optionRemove.setEnabled(false);
                break;
            case LOCALE_CHANGED:
                bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
                menuFile.setText(bundle.getString("file"));
                menuEdit.setText(bundle.getString("edit"));
                optionOpen.setText(bundle.getString("open"));
                optionPlace.setText(bundle.getString("place"));
                optionRemove.setText(bundle.getString("remove.selected"));
                optionSaveAs.setText(bundle.getString("save.as"));
                optionClose.setText(bundle.getString("close.document"));
                optionExit.setText(bundle.getString("quit"));
                optionSave.setText(bundle.getString("save"));
                optionSettings.setText(bundle.getString("settings"));
                pageSubmenu.setText(bundle.getString("page"));
                overlaySubmenu.setText(bundle.getString("overlay"));
                optionPlace.setText(bundle.getString("place"));
                optionRemove.setText(bundle.getString("remove.selected"));
                optionRemAllOverlays.setText(bundle.getString("remove.all.overlays"));
                optionAddPages.setText(bundle.getString("add.pages"));
                optionRemovePage.setText(bundle.getString("delete"));
                optionPageToFront.setText(bundle.getString("make.first"));
                optionPageToLeft.setText(bundle.getString("move.left"));
                optionPageToRight.setText(bundle.getString("move.right"));
                optionPageToBack.setText(bundle.getString("make.last"));
                optionRotLeft.setText(bundle.getString("rotate.left"));
                optionRotRight.setText(bundle.getString("rotate.right"));
                break;
            case FILE_UNMODIFIED:
                optionSaveAs.setEnabled(false);
                optionSave.setEnabled(false);
                break;
            case FILE_MODIFIED:
                optionSaveAs.setEnabled(true);
                optionSave.setEnabled(true);

        }
    }
}
