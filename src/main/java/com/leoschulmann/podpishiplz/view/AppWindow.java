package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.GUIController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Properties;

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

        String title = "BitteUnterschreiben";
        Properties prop = new Properties();
        try {
            prop.load(AppWindow.class.getClassLoader().getResourceAsStream("META-INF/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            LoggerFactory.getLogger(AppWindow.class).warn("Can't load app.properties");
        }
        if (prop.getProperty("app.version") != null) {
            title = title + " v" + prop.getProperty("app.version");
        }

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
        menuBar.getOptionSaveAs().addActionListener(e -> GUIController.saveFile(this));
        menuBar.getOptionSettings().addActionListener(e -> GUIController.openSettingsDialogue());

        add(topScrollerPanel.getWrapper(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(overlayPanel, BorderLayout.SOUTH);
        LoggerFactory.getLogger(AppWindow.class).debug("Finished building {}", this.getClass().getSimpleName());
    }
}
