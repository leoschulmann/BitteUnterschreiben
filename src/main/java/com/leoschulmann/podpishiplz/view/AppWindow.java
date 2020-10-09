package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.controller.DocumentController;
import com.leoschulmann.podpishiplz.controller.GUIController;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    private final MainPanel mainPanel;
    private final TopScrollerPanel topScrollerPanel;
    private final OverlayPanel overlayPanel;

    public AppWindow() throws HeadlessException {

        String OSDesign = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(OSDesign);  //sets OS-specific Look and Feel
            if (OSDesign.contains("apple")) {
                LoggerFactory.getLogger(AppWindow.class).info("Creating MacOS interface look and feel.");
                System.setProperty("apple.laf.useScreenMenuBar", "true");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getClass().toString()
                    + " " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LoggerFactory.getLogger(AppWindow.class).error(e.getMessage(), e);
        }

        String title = SettingsController.getDefaultProducer();

        setTitle(title);
        setSize(new Dimension(850, 600));
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null); //center on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new MainPanel();
        topScrollerPanel = new TopScrollerPanel();
        overlayPanel = new OverlayPanel();

        MenuBar menuBar = new MenuBar();
        setJMenuBar(menuBar);

        menuBar.getOptionOpen().addActionListener(e -> GUIController.openOption(this));
        menuBar.getOptionPlace().addActionListener(e -> GUIController.placeOption(this));
        menuBar.getOptionRemove().addActionListener(e -> GUIController.deleteSelectedOverlayOption());
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

        //todo implement
        menuBar.getOptionAddPages().addActionListener(e -> JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(),
                "Under construction", "Message", JOptionPane.INFORMATION_MESSAGE));
        menuBar.getOptionRemAllOverlays().addActionListener(e -> JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(),
                "Under construction", "Message", JOptionPane.INFORMATION_MESSAGE));

        add(topScrollerPanel.getWrapper(), BorderLayout.NORTH);
        add(mainPanel.getMainPanelWrapper(), BorderLayout.CENTER);
        add(overlayPanel.getWrapper(), BorderLayout.SOUTH);
        LoggerFactory.getLogger(AppWindow.class).debug("Finished building {}", this.getClass().getSimpleName());
    }
}
