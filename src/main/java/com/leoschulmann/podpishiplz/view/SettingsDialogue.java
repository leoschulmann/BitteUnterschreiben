package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;

import javax.swing.*;
import java.awt.*;

public class SettingsDialogue extends JDialog {
    private static JRadioButton noopBlender;
    public static JRadioButton darkenBlender;
    public static JRadioButton multiplyBlender;
    public static JRadioButton[] btns;


    public SettingsDialogue(JFrame appWindow) {
        setTitle("Настройки");
        setModal(true);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //init components
        ButtonGroup radioGroup = initRadios(SettingsController.getBlendingMode());
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> confirm(btns));
        getRootPane().setDefaultButton(ok);
        JButton cancel = new JButton("cancel");
        cancel.addActionListener(e -> setVisible(false));

        //placeholder
        ImageIcon icon = new ImageIcon(appWindow.getClass().getClassLoader().getResource("pholder.png"));

        //draw components
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(wrapPanel(new JLabel(null, icon, JLabel.CENTER)), gbc); //todo implement clickable

        gbc.gridx++;
        add(wrapPanel(new JLabel(null, icon, JLabel.CENTER)), gbc);

        gbc.gridx++;
        add(wrapPanel(new JLabel(null, icon, JLabel.CENTER)), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(noopBlender, gbc);

        gbc.gridx++;
        add(darkenBlender, gbc);

        gbc.gridx++;
        add(multiplyBlender, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(wrapPanel(ok, cancel), gbc);
        pack();
        setLocationRelativeTo(appWindow);
    }

    private JPanel wrapPanel(JButton ok, JButton cancel) {
        JPanel jp = new JPanel();
        jp.add(ok);
        jp.add(cancel);
        return jp;
    }

    private JPanel wrapPanel(JLabel jLabel) {
        JPanel jp = new JPanel();
        jp.add(jLabel);
        return jp;
    }

    private void confirm(JRadioButton[] btns) {
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                SettingsController.setBlendingMode(i);
                this.setVisible(false);
                break;
            }
        }
    }

    private ButtonGroup initRadios(int i) {
        //todo load from yml file
        ButtonGroup buttonGroup = new ButtonGroup();
        noopBlender = new JRadioButton("Без смешивания");
        darkenBlender = new JRadioButton("Смешивание с затемнением");
        multiplyBlender = new JRadioButton("Смешивание с умножением");
        btns = new JRadioButton[]{noopBlender, darkenBlender, multiplyBlender};
        buttonGroup.add(noopBlender);
        buttonGroup.add(darkenBlender);
        buttonGroup.add(multiplyBlender);
        buttonGroup.clearSelection();
        btns[i].setSelected(true);
        return buttonGroup;
    }
}
