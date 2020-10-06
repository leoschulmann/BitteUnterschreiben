package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.controller.EventListener;
import com.leoschulmann.podpishiplz.controller.EventType;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class SettingsBlending extends JPanel implements SettingsTab, EventListener {
    private static ImageIcon noBlendingIcon;
    private static ImageIcon darkenBlendingIcon;
    private static ImageIcon multiplyBlendingIcon;
    private static JRadioButton noopBlender;
    public static JRadioButton darkenBlender;
    public static JRadioButton multiplyBlender;
    public static JRadioButton[] btns;
    private static JPanel dummy1;
    private static JPanel dummy2;
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());


    public SettingsBlending() {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.CENTER;
        init();

        add(new JLabel(null, noBlendingIcon, JLabel.CENTER), g);
        g.gridx++;
        add(dummy1, g);
        g.gridx++;
        add(new JLabel(null, darkenBlendingIcon, JLabel.CENTER), g);
        g.gridx++;
        add(dummy2, g);
        g.gridx++;
        add(new JLabel(null, multiplyBlendingIcon, JLabel.CENTER), g);
        g.gridx = 0;
        g.gridy++;
        add(noopBlender, g);
        g.gridx++;
        g.gridx++;
        add(darkenBlender, g);
        g.gridx++;
        g.gridx++;
        add(multiplyBlender, g);
    }

    @Override
    public void init() {
        noBlendingIcon = new ImageIcon(BitteUnterschreiben.getApp().getClass().getClassLoader()
                .getResource("no_blending.png"));
        darkenBlendingIcon = new ImageIcon(BitteUnterschreiben.getApp().getClass().getClassLoader()
                .getResource("darken_blending.png"));
        multiplyBlendingIcon = new ImageIcon(BitteUnterschreiben.getApp().getClass().getClassLoader()
                .getResource("multiply_blending.png"));

        ButtonGroup buttonGroup = new ButtonGroup();
        noopBlender = new JRadioButton(bundle.getString("no.blending"));
        darkenBlender = new JRadioButton(bundle.getString("darken"));
        multiplyBlender = new JRadioButton(bundle.getString("multiply"));
        btns = new JRadioButton[]{noopBlender, darkenBlender, multiplyBlender};
        buttonGroup.add(noopBlender);
        buttonGroup.add(darkenBlender);
        buttonGroup.add(multiplyBlender);
        buttonGroup.clearSelection();
        if (SettingsController.getBlendingMode() != -1) {
            btns[SettingsController.getBlendingMode()].setSelected(true);
        }

        dummy1 = new JPanel();
        dummy2 = new JPanel();
        dummy1.setSize(10, 10);
        dummy2.setSize(10, 10);
    }

    @Override
    public void saveState() {
        int mode = IntStream.range(0, btns.length).filter(i -> btns[i].isSelected()).findFirst().orElse(-1);
        SettingsController.setBlendingMode(mode);
        LoggerFactory.getLogger(SettingsBlending.class).debug(
                "Saving data: blending mode {}", mode);
    }

    @Override
    public String getTitle() {
        return bundle.getString("blending");
    }

    @Override
    public void eventUpdate(EventType event, Object object) {
        if (event == EventType.LOCALE_CHANGED) {
            bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
            noopBlender.setText("no.blending");
            darkenBlender.setText("darken");
            multiplyBlender.setText("multiply");
        }
    }
}
