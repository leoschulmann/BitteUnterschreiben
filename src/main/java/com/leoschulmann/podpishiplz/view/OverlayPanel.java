package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.*;

public class OverlayPanel extends JPanel {
    private final GridBagConstraints gbc;

    public OverlayPanel() {
        setPreferredSize(new Dimension(100, 70));

        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
    }

    public void put(Component c) {
        add(c, gbc);
        gbc.gridx++;
    }
}
