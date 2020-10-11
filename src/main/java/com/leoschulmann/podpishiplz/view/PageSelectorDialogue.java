package com.leoschulmann.podpishiplz.view;

import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class PageSelectorDialogue extends JDialog {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
    private boolean[] result;

    public PageSelectorDialogue(Frame owner, String file, PageSelectorElement[] pages) {
        super(owner);
        setTitle(bundle.getString("select.pages"));
        setModal(true);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel pagesBlock = new JPanel(new FlowLayout());
        JScrollPane pagesWrapper = new JScrollPane(pagesBlock);
        pagesWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        pagesWrapper.setBorder(BorderFactory.createEmptyBorder());
        Arrays.stream(pages).forEach(pagesBlock::add);
        add(pagesWrapper, BorderLayout.CENTER);

        JPanel okCancelBlock = new JPanel(new FlowLayout());
        JButton ok = new JButton(bundle.getString("ok"));
        JButton cancel = new JButton(bundle.getString("cancel"));
        JButton selectAll = new JButton(bundle.getString("select.all"));
        okCancelBlock.add(ok);
        okCancelBlock.add(selectAll);
        okCancelBlock.add(cancel);
        add(okCancelBlock, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(ok);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pack();
        setSize(500, getHeight() + 10);
        setLocationRelativeTo(owner);
        LoggerFactory.getLogger(PageSelectorDialogue.class).debug("Dialogue created [{}x{}]", getWidth(), getHeight());

        ok.addActionListener(e -> {
            result = new boolean[pages.length];
            for (int i = 0; i < pages.length; i++) result[i] = pages[i].isSelected();
            setVisible(false);
            dispose();
        });
        selectAll.addActionListener(e -> Arrays.stream(pages).forEach(p -> p.setSelected(true)));
        cancel.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    public boolean[] showDialog() {
        setVisible(true);
        return result;
    }
}
