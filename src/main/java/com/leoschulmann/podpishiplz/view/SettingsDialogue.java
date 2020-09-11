package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
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
    private static ImageIcon noBlendingIcon;
    private static ImageIcon darkenBlendingIcon;
    private static ImageIcon multiplyBlendingIcon;


    public SettingsDialogue(JFrame appWindow, int blendingMode, float jpgQuality, float resolution) {
        setTitle("Настройки");
        setModal(true);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //init components
        initRadios(blendingMode);
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> confirm());
        getRootPane().setDefaultButton(ok);
        JButton cancel = new JButton("Отмена");
        cancel.addActionListener(e -> setVisible(false));
        jpegQltySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) (jpgQuality * 100));
        jpegQltySlider.setMajorTickSpacing(20);
        jpegQltySlider.setMinorTickSpacing(10);
        jpegQltySlider.setSnapToTicks(true);
        jpegQltySlider.setPaintTicks(true);
        jpegQltySlider.setPaintLabels(true);

        dpiSlider = new JSlider(JSlider.HORIZONTAL, 100, 600, (int) (resolution * 300));
        dpiSlider.setMajorTickSpacing(100);
        dpiSlider.setMinorTickSpacing(50);
        dpiSlider.setSnapToTicks(true);
        dpiSlider.setPaintTicks(true);
        dpiSlider.setPaintLabels(true);

        //images
        noBlendingIcon = new ImageIcon(appWindow.getClass().getClassLoader().getResource("no_blending.png"));
        darkenBlendingIcon = new ImageIcon(appWindow.getClass().getClassLoader().getResource("darken_blending.png"));
        multiplyBlendingIcon = new ImageIcon(appWindow.getClass().getClassLoader().getResource("multiply_blending.png"));

        //dummy
        JPanel dummy = new JPanel();
        dummy.setPreferredSize(new Dimension(40, 10));

        //draw components
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(wrapPanel(true, drawBlendingRadio()), gbc);

        gbc.gridy++;
        add(wrapPanel(true, new JLabel("Степень сжатия JPEG"), dummy, jpegQltySlider), gbc);

        gbc.gridy++;
        add(wrapPanel(true, new JLabel("Разрешение изображений, dpi"), dpiSlider), gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(wrapPanel(false, ok, cancel), gbc);

        pack();
        setLocationRelativeTo(appWindow);
    }

    static private JPanel drawBlendingRadio() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        JPanel noBlendingPanel = new JPanel(new BorderLayout());
        noBlendingPanel.add(new JLabel(null, SettingsDialogue.noBlendingIcon, JLabel.CENTER), BorderLayout.CENTER);
        noBlendingPanel.add(noopBlender, BorderLayout.SOUTH);

        JPanel darkenBlendingPanel = new JPanel(new BorderLayout());
        darkenBlendingPanel.add(new JLabel(null, SettingsDialogue.darkenBlendingIcon, JLabel.CENTER), BorderLayout.CENTER);
        darkenBlendingPanel.add(darkenBlender, BorderLayout.SOUTH);

        JPanel multiplyBlendingPanel = new JPanel(new BorderLayout());
        multiplyBlendingPanel.add(new JLabel(null, SettingsDialogue.multiplyBlendingIcon, JLabel.CENTER), BorderLayout.CENTER);
        multiplyBlendingPanel.add(multiplyBlender, BorderLayout.SOUTH);

        panel.add(noBlendingPanel);
        panel.add(darkenBlendingPanel);
        panel.add(multiplyBlendingPanel);

        return panel;
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

    private JPanel wrapPanel(boolean bordered, Component... components) {
        JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (Component c : components) {
            jp.add(c);
        }
        if (bordered) jp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        JPanel outer = new JPanel();
        outer.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        outer.add(jp);
        return outer;
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
            LoggerFactory.getLogger(SettingsDialogue.class).error(e.getMessage(), e);
        }
    }

    private void initRadios(int i) {
        ButtonGroup buttonGroup = new ButtonGroup();
        noopBlender = new JRadioButton("Без смешения");
        darkenBlender = new JRadioButton("Смешение с затемнением");
        multiplyBlender = new JRadioButton("Смешение с умножением");
        btns = new JRadioButton[]{noopBlender, darkenBlender, multiplyBlender};
        buttonGroup.add(noopBlender);
        buttonGroup.add(darkenBlender);
        buttonGroup.add(multiplyBlender);
        buttonGroup.clearSelection();
        btns[i].setSelected(true);
    }
}
