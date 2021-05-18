package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.MenuBarController;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class AppWindow extends JFrame {
    private final String title;

    public AppWindow() throws HeadlessException {

        String OSDesign = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(OSDesign);  //sets OS-specific Look and Feel
            if (OSDesign.contains("apple")) {
                log.info("Creating MacOS interface look and feel.");
                System.setProperty("apple.laf.useScreenMenuBar", "true");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getClass().toString()
                    + " " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            log.error(e.getMessage(), e);
        }

        title = SettingsController.getDefaultProducer();

        setTitle(title);
        setSize(new Dimension(850, 850));
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null); //center on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        MainPanel mainPanel = new MainPanel();
        TopScrollerPanel topScrollerPanel = new TopScrollerPanel();
        OverlayPanel overlayPanel = new OverlayPanel();
        setJMenuBar(MenuBarController.getMenuBar());
        add(topScrollerPanel.getWrapper(), BorderLayout.NORTH);
        add(mainPanel.getMainPanelWrapper(), BorderLayout.CENTER);
        add(overlayPanel.getWrapper(), BorderLayout.SOUTH);
        log.debug("Finished building {}", this.getClass().getSimpleName());
    }

    public void setFrameTitle(String title) {
        setTitle(title);
    }

    public void resetFrameTitle() {
        setTitle(title);
    }
}
