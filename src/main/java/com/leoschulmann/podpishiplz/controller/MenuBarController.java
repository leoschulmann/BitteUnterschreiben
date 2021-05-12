package com.leoschulmann.podpishiplz.controller;

import com.leoschulmann.podpishiplz.view.MenuBar;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuBarController {
    private static MenuBar menuBar;
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());

    public static JMenuBar getMenuBar() {
        return menuBar == null ? getInstance() : menuBar;
    }

    private static JMenuBar getInstance() {
        menuBar = new MenuBar();

        menuBar.getOptionOpen().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        menuBar.getOptionSave().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        menuBar.getOptionSaveAs().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        menuBar.getOptionPlace().setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        menuBar.getOptionRemove().setAccelerator(KeyStroke.getKeyStroke("BACK_SPACE"));
        menuBar.getOptionSettings().setAccelerator(KeyStroke.getKeyStroke("F12"));

        menuBar.getOptionOpen().addActionListener(e -> GUIController.openOption());
        menuBar.getOptionPlace().addActionListener(e -> GUIController.placeOption());
        menuBar.getOptionRemove().addActionListener(e -> GUIController.deleteSelectedOverlayOption());
        menuBar.getOptionRemAllOverlays().addActionListener(e -> GUIController.deleteAllOverlaysOption());
        menuBar.getOptionSaveAs().addActionListener(e -> GUIController.saveFileAs());
        menuBar.getOptionSave().addActionListener(e -> GUIController.saveFile());
        menuBar.getOptionExit().addActionListener(e -> GUIController.quit());
        menuBar.getOptionClose().addActionListener(e -> GUIController.closeDocument());
        menuBar.getOptionSettings().addActionListener(e -> GUIController.openSettingsDialogue());

        menuBar.getOptionPageToFront().addActionListener(
                e -> DocumentController.movePageToFront(DocumentController.getCurrentPage()));
        menuBar.getOptionPageToLeft().addActionListener(
                e -> DocumentController.movePageLeft(DocumentController.getCurrentPage()));
        menuBar.getOptionPageToRight().addActionListener(
                e -> DocumentController.movePageRight(DocumentController.getCurrentPage()));
        menuBar.getOptionPageToBack().addActionListener(
                e -> DocumentController.movePageToBack(DocumentController.getCurrentPage()));
        menuBar.getOptionRemovePage().addActionListener(
                e -> DocumentController.deletePage(DocumentController.getCurrentPage()));

        menuBar.getOptionRotLeft().addActionListener(
                e -> DocumentController.rotateLeft(DocumentController.getCurrentPage(), true));
        menuBar.getOptionRotRight().addActionListener(
                e -> DocumentController.rotateLeft(DocumentController.getCurrentPage(), false));
        menuBar.getOptionAddPages().addActionListener(e -> GUIController.addPagesFromFileOption());

        return menuBar;
    }

    public static void initListener() {
        EventListener el = (event, object) -> {
            switch (event) {
                case MAIN_PANEL_FULL:
                    menuBar.getOptionPlace().setEnabled(true);
                    menuBar.getOptionRemAllOverlays().setEnabled(true);
                    break;
                case MAIN_PANEL_EMPTY:
                    menuBar.getOverlayOptions().forEach(e -> e.setEnabled(false));
                    menuBar.getPageOptions().forEach(e -> e.setEnabled(false));
                    break;
                case PAGES_ADDED:
                    menuBar.getOptionClose().setEnabled(true);
                    menuBar.getPageOptions().forEach(e -> e.setEnabled(true));
                    break;
                case NO_PAGES_IN_DOCUMENT:
                    menuBar.getOptionSaveAs().setEnabled(false);
                    menuBar.getOptionSave().setEnabled(false);
                    menuBar.getOptionClose().setEnabled(false);
                    menuBar.getOptionClose().setEnabled(false);
                    menuBar.getOverlayOptions().forEach(e -> e.setEnabled(false));
                    menuBar.getPageOptions().forEach(e -> e.setEnabled(false));
                    break;
                case OVERLAY_SELECTED:
                    menuBar.getOptionRemove().setEnabled(true);
                    break;
                case OVERLAY_DESELECTED:
                    menuBar.getOptionRemove().setEnabled(false);
                    break;
                case LOCALE_CHANGED:
                    bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
                    menuBar.getMenuFile().setText(bundle.getString("file"));
                    menuBar.getMenuEdit().setText(bundle.getString("edit"));
                    menuBar.getOptionOpen().setText(bundle.getString("open"));
                    menuBar.getOptionPlace().setText(bundle.getString("place"));
                    menuBar.getOptionRemove().setText(bundle.getString("remove.selected"));
                    menuBar.getOptionSaveAs().setText(bundle.getString("save.as"));
                    menuBar.getOptionClose().setText(bundle.getString("close.document"));
                    menuBar.getOptionExit().setText(bundle.getString("quit"));
                    menuBar.getOptionSave().setText(bundle.getString("save"));
                    menuBar.getOptionSettings().setText(bundle.getString("settings"));
                    menuBar.getPageSubmenu().setText(bundle.getString("page"));
                    menuBar.getOverlaySubmenu().setText(bundle.getString("overlay"));
                    menuBar.getOptionPlace().setText(bundle.getString("place"));
                    menuBar.getOptionRemove().setText(bundle.getString("remove.selected"));
                    menuBar.getOptionRemAllOverlays().setText(bundle.getString("remove.all.overlays"));
                    menuBar.getOptionAddPages().setText(bundle.getString("add.pages"));
                    menuBar.getOptionRemovePage().setText(bundle.getString("delete"));
                    menuBar.getOptionPageToFront().setText(bundle.getString("make.first"));
                    menuBar.getOptionPageToLeft().setText(bundle.getString("move.left"));
                    menuBar.getOptionPageToRight().setText(bundle.getString("move.right"));
                    menuBar.getOptionPageToBack().setText(bundle.getString("make.last"));
                    menuBar.getOptionRotLeft().setText(bundle.getString("rotate.left"));
                    menuBar.getOptionRotRight().setText(bundle.getString("rotate.right"));
                    break;
                case FILE_UNMODIFIED:
                    menuBar.getOptionSaveAs().setEnabled(false);
                    menuBar.getOptionSave().setEnabled(false);
                    break;
                case FILE_MODIFIED:
                    menuBar.getOptionSaveAs().setEnabled(true);
                    menuBar.getOptionSave().setEnabled(true);
            }
        };
        EventController.subscribe(EventType.MAIN_PANEL_EMPTY, el);
        EventController.subscribe(EventType.MAIN_PANEL_FULL, el);
        EventController.subscribe(EventType.PAGES_ADDED, el);
        EventController.subscribe(EventType.NO_PAGES_IN_DOCUMENT, el);
        EventController.subscribe(EventType.OVERLAY_DESELECTED, el);
        EventController.subscribe(EventType.OVERLAY_SELECTED, el);
        EventController.subscribe(EventType.LOCALE_CHANGED, el);
        EventController.subscribe(EventType.FILE_MODIFIED, el);
        EventController.subscribe(EventType.FILE_UNMODIFIED, el);
    }
}
