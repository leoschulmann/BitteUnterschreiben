package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.MenuBarController;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    private final MainPanel mainPanel;
    private final TopScrollerPanel topScrollerPanel;
    private final OverlayPanel overlayPanel;
    private final String title;

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

        title = SettingsController.getDefaultProducer();

        setTitle(title);
        setSize(new Dimension(850, 850));
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null); //center on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new MainPanel();
        topScrollerPanel = new TopScrollerPanel();
        overlayPanel = new OverlayPanel();
        setJMenuBar(MenuBarController.getMenuBar());
        add(topScrollerPanel.getWrapper(), BorderLayout.NORTH);
        add(mainPanel.getMainPanelWrapper(), BorderLayout.CENTER);
        add(overlayPanel.getWrapper(), BorderLayout.SOUTH);
        LoggerFactory.getLogger(AppWindow.class).debug("Finished building {}", this.getClass().getSimpleName());
    }

    public void setFrameTitle(String title) {
        setTitle(title);
    }

    public void resetFrameTitle() {
        setTitle(title);
    }
}
