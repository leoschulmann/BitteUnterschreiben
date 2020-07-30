package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.GUIController;

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

        setTitle("Подпиши плз");
        setSize(new Dimension(850, 600));
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null); //center on screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        MenuBar menuBar = new MenuBar();

        setJMenuBar(menuBar);
        TopScrollerPanel topScrollerPanel = new TopScrollerPanel();

        menuBar.getOptionOpen().addActionListener(e -> GUIController.openOption(this, topScrollerPanel));
        menuBar.getOptionPlace().addActionListener(e -> GUIController.placeOption(this));

        add(topScrollerPanel.getWrapper(), BorderLayout.NORTH);
        MainPanel mainPanel = new MainPanel();
        GUIController.setMainPanel(mainPanel);  //todo should switch to Spring...
        add(mainPanel, BorderLayout.CENTER);
    }
}
