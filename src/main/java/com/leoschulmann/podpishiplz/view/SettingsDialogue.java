package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.EventController;
import com.leoschulmann.podpishiplz.controller.EventListener;
import com.leoschulmann.podpishiplz.controller.EventType;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class SettingsDialogue extends JDialog implements EventListener {
    private final java.util.List<SettingsTab> tabList;
    private final JButton cancel;
    private final JButton ok;
    private final JTabbedPane tabs;

    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());

    public SettingsDialogue(Frame owner) {
        super(owner);
        setName(bundle.getString("settings"));
        setModal(true);
        setResizable(false);

        tabList = new ArrayList<>();

        tabList.add(new SettingsGraphics());
        tabList.add(new SettingsUI());
        tabList.add(new SettingsBlending());
        tabList.add(new SettingsPDFMetadata());

        tabs = new JTabbedPane();
        manageTabs(tabList);

        JPanel okCancel = new JPanel();
        cancel = new JButton(bundle.getString("cancel"));
        ok = new JButton(bundle.getString("ok"));
        getRootPane().setDefaultButton(ok);
        cancel.addActionListener(e -> this.setVisible(false));
        ok.addActionListener(e -> confirm());
        okCancel.add(ok);
        okCancel.add(cancel);

        add(tabs, BorderLayout.CENTER);
        add(okCancel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);

        //tabs should subscribe first since they should be refreshed (notified) earlier
        for (SettingsTab st : tabList) {
            EventController.subscribe(EventType.LOCALE_CHANGED, st);
        }
        EventController.subscribe(EventType.LOCALE_CHANGED, this);
    }

    private void manageTabs(List<SettingsTab> tabList) {
        tabs.removeAll();
        for (SettingsTab st : tabList) {
            log.debug("Adding {}", st.getTitle());
            tabs.add(st.getTitle(), (JPanel) st);
        }
    }

    private void confirm() {
        tabList.forEach(SettingsTab::saveState);

        try {
            SettingsController.saveYML();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            log.error(e.getMessage(), e);
        }
        setVisible(false);
    }

    @Override
    public void eventUpdate(EventType event, Object object) {
        if (event == EventType.LOCALE_CHANGED) {
            bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
            setName(bundle.getString("settings"));
            cancel.setText(bundle.getString("cancel"));
            ok.setText(bundle.getString("ok"));

            manageTabs(tabList);
        }
    }
}
