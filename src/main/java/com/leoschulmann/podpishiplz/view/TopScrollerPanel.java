package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.PDFController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TopScrollerPanel {
    private final JPanel panel;
    private final JScrollPane wrapper;

    public TopScrollerPanel() {
        panel = new JPanel(new FlowLayout());
        wrapper = new JScrollPane(panel);
        wrapper.setPreferredSize(new Dimension(100, 120));
    }

    public void loadFile(String file) throws IOException {
        BufferedImage[] images = PDFController.loadPDF(file);
        for (BufferedImage image : images) {
            JButton jb = new JButton(new ImageIcon(image));
            panel.add(jb);
        }
        panel.revalidate();
        panel.repaint();
    }

    public JScrollPane getWrapper() {
        return wrapper;
    }
}
