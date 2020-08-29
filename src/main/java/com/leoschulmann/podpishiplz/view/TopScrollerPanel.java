package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.*;

public class TopScrollerPanel {
    private final JPanel panel;
    private final JScrollPane wrapper;
    private final GridBagConstraints gbc;

    public TopScrollerPanel() {
        panel = new JPanel(new GridBagLayout());
        wrapper = new JScrollPane(panel);
        wrapper.setPreferredSize(new Dimension(100, 120));
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
    }

    public JScrollPane getWrapper() {
        return wrapper;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void rem(Component c) {
        panel.remove(c);
        panel.revalidate();
        panel.repaint();
    }

    public void put(Component c) {
        panel.add(c, gbc);
        gbc.gridx++;
    }
}
