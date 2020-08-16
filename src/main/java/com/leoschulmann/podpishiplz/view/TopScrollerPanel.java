package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TopScrollerPanel {
    private final JPanel panel;
    private final JScrollPane wrapper;

    public TopScrollerPanel() {
        panel = new JPanel(new FlowLayout());
        wrapper = new JScrollPane(panel);
        wrapper.setPreferredSize(new Dimension(100, 120));
    }

    public JScrollPane getWrapper() {
        return wrapper;
    }

    public JButton addNewButton(BufferedImage th) {
        JButton jb = new JButton(new ImageIcon(th));
        panel.add(jb);
        panel.revalidate();
        panel.repaint();
        return jb;
    }
}
