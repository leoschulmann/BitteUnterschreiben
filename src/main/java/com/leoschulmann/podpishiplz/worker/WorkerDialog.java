package com.leoschulmann.podpishiplz.worker;

import javax.swing.*;
import java.awt.*;

public class WorkerDialog extends JDialog {
    public WorkerDialog(Frame owner, String text) {
        add(new JLabel(text));
        setLocationRelativeTo(owner);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();
    }
}
