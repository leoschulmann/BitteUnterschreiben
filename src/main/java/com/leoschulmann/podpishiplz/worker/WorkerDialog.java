package com.leoschulmann.podpishiplz.worker;

import javax.swing.*;
import java.awt.*;

public class WorkerDialog extends JDialog {
    private final JLabel label;

    public WorkerDialog(Frame owner, String text) {
        setUndecorated(true);
        label = new JLabel(text);
        add(label);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);
    }

    protected void setText(String text) {
        remove(label);
        label.setText(text);
        add(label);
        revalidate();
        repaint();
        pack();
    }
}
