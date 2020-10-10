package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PageSelectorElement extends JPanel {
    private final JCheckBox checkBox;

    public PageSelectorElement(BufferedImage thumbnail) {
        JLabel preview = new JLabel(new ImageIcon(thumbnail));
        this.checkBox = new JCheckBox();
        preview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkBox.setSelected(!checkBox.isSelected());
            }
        });
        setLayout(new BorderLayout());
        add(preview, BorderLayout.CENTER);
        add(checkBox, BorderLayout.SOUTH);
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }
}
