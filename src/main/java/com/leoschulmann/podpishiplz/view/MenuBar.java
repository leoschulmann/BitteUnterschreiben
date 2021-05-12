package com.leoschulmann.podpishiplz.view;

import lombok.Getter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
@Getter
public class MenuBar extends JMenuBar {
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
    private final Set<JMenuItem> pageOptions;
    private final Set<JMenuItem> overlayOptions;
    private static final ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());

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

    }
}
