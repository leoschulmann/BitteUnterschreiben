package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.OverlaysPanelController;

import javax.swing.*;
import java.awt.*;

public class OverlayPanel {
    private final GridBagConstraints gbc;
    private final JPanel panel;
    private final JScrollPane wrapper;

    OverlayPanel() {
        panel = new JPanel(new GridBagLayout());
        wrapper = new JScrollPane(panel);
        wrapper.setPreferredSize(new Dimension(1, 80));
        wrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        OverlaysPanelController.setPanel(this);

        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
    }

    public void put(Component c) {
        panel.add(c, gbc);
        gbc.gridx++;
    }

    JScrollPane getWrapper() {
        return wrapper;
    }

    public void revalidate() {
        panel.revalidate();
    }

    public void repaint() {
        panel.repaint();
    }

    public void removeAll() {
        panel.removeAll();
    }

    public Component[] getComponents() {
       return panel.getComponents();
    }
}
