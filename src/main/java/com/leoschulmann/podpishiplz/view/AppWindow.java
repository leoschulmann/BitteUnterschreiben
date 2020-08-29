package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.*;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    public AppWindow() throws HeadlessException {

        String OSDesign = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(OSDesign);  //sets OS-specific Look and Feel
            if (OSDesign.contains("apple")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");  // sets MacOS-specific top menu bar
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getClass().toString()
                    + " " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setTitle("Bitte Unterschreiben");
        setSize(new Dimension(850, 600));
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null); //center on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        MenuBar menuBar = new MenuBar();

        setJMenuBar(menuBar);
        TopScrollerPanel topScrollerPanel = new TopScrollerPanel();

        menuBar.getOptionOpen().addActionListener(e -> GUIController.openOption(this));
        menuBar.getOptionPlace().addActionListener(e -> GUIController.placeOption(this));
        menuBar.getOptionRemove().addActionListener(e -> GUIController.deleteSelectedOverlayOption());
        menuBar.getOptionSaveAs().addActionListener(e -> GUIController.saveFile(this));
        menuBar.getOptionSettings().addActionListener(e -> GUIController.openSettingsDialogue(this));

        add(topScrollerPanel.getWrapper(), BorderLayout.NORTH);
        MainPanel mainPanel = new MainPanel();
        GUIController.setMainPanel(mainPanel);  //todo should switch to Spring...
        GUIController.setTopScrollerPanel(topScrollerPanel);
        GUIController.setAppWindow(this);
        MainPanelController.setMainPanel(mainPanel);
        add(mainPanel, BorderLayout.CENTER);

        DocumentController.createDocument();
        EventController.notify(EventType.MAIN_PANEL_EMPTY, null);
        EventController.notify(EventType.NO_PAGES_IN_DOCUMENT, null);
    }
}
