package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SettingsDialogue extends JDialog {
    private static JRadioButton noopBlender;
    public static JRadioButton darkenBlender;
    public static JRadioButton multiplyBlender;
    public static JRadioButton[] btns;
    public static JSlider jpegQltySlider;
    public static JSlider dpiSlider;
    private int blendingMode;
    private float jpgQuality;
    private float resolution;

    public SettingsDialogue(JFrame appWindow, int blendingMode, float jpgQuality, float resolution) {
        this.blendingMode = blendingMode;
        this.jpgQuality = jpgQuality;
        this.resolution = resolution;
        setTitle("Настройки");
        setModal(true);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //init components
        ButtonGroup radioGroup = initRadios(blendingMode);
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> confirm());
        getRootPane().setDefaultButton(ok);
        JButton cancel = new JButton("cancel");
        cancel.addActionListener(e -> setVisible(false));
        jpegQltySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (jpgQuality * 100));
        jpegQltySlider.setMajorTickSpacing(20);
        jpegQltySlider.setMinorTickSpacing(10);
        jpegQltySlider.setSnapToTicks(true);
        jpegQltySlider.setPaintTicks(true);
        jpegQltySlider.setPaintLabels(true);

        dpiSlider = new JSlider(JSlider.HORIZONTAL, 100, 600, (int) (resolution * 300));
        dpiSlider.setMajorTickSpacing(100);
        dpiSlider.setSnapToTicks(true);
        dpiSlider.setPaintTicks(true);
        dpiSlider.setPaintLabels(true);

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

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Степень сжатия JPEG"), gbc);
        gbc.gridx++;
        add(jpegQltySlider, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Разрешение изображений, dpi"), gbc);
        gbc.gridx++;
        add(dpiSlider, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(wrapPanel(ok, cancel), gbc);
        pack();
        setLocationRelativeTo(appWindow);
    }

    public void setBlendingMode(int blendingMode) {
        this.blendingMode = blendingMode;
    }

    public void setJpgQuality(float jpgQuality) {
        this.jpgQuality = jpgQuality;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
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

    private void confirm() {
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                SettingsController.setBlendingMode(i);
                this.setVisible(false);
                break;
            }
        }
        SettingsController.setJpegQuality(1.0f * jpegQltySlider.getValue() / 100);
        SettingsController.setResolutionMultiplier(1.0f * dpiSlider.getValue() / 300);
        try {
            SettingsController.saveYML();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private ButtonGroup initRadios(int i) {
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
