package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.GUIController;

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

        mainPanel = new MainPanel();
        topScrollerPanel = new TopScrollerPanel();
        overlayPanel = new OverlayPanel();

        MenuBar menuBar = new MenuBar();
        setJMenuBar(menuBar);

        menuBar.getOptionOpen().addActionListener(e -> GUIController.openOption(this));
        menuBar.getOptionPlace().addActionListener(e -> GUIController.placeOption(this));
        menuBar.getOptionRemove().addActionListener(e -> GUIController.deleteSelectedOverlayOption());
        menuBar.getOptionSaveAs().addActionListener(e -> GUIController.saveFile(this));
        menuBar.getOptionSettings().addActionListener(e -> GUIController.openSettingsDialogue());

        add(topScrollerPanel.getWrapper(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(overlayPanel, BorderLayout.SOUTH);
    }

    public MainPanel getMainPanel() {  // only for MainPanelController
        return mainPanel;
    }

    public TopScrollerPanel getTopScrollerPanel() { // only for TopPanelController
        return topScrollerPanel;
    }

    public OverlayPanel getOverlayPanel() {  // only for OverlayPanelController         //todo fix this mess
        return overlayPanel;
    }
}
