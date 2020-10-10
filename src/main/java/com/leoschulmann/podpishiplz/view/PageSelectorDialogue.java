package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.FileIOController;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class PageSelectorDialogue extends JDialog {
    private static ResourceBundle bundle = ResourceBundle.getBundle("lang", Locale.getDefault());
    private boolean[] result;
    private final PageSelectorElement[] pages;

    public PageSelectorDialogue(Frame owner, String file) {
        super(owner);
        setTitle(bundle.getString("select.pages"));
        setModal(true);
        setResizable(false);
        setLayout(new BorderLayout());

        pages = initPages(file);
        JPanel pagesBlock = new JPanel(new FlowLayout());
        JScrollPane pagesWrapper = new JScrollPane(pagesBlock);
        pagesWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        Arrays.stream(pages).forEach(pagesBlock::add);
        add(pagesWrapper, BorderLayout.CENTER);

        JPanel okCancelBlock = new JPanel(new FlowLayout());
        JButton ok = new JButton(bundle.getString("ok"));
        JButton cancel = new JButton(bundle.getString("cancel"));
        okCancelBlock.add(ok);
        okCancelBlock.add(cancel);
        add(okCancelBlock, BorderLayout.SOUTH);

        pack();
        setSize(500, getHeight()+10);
        setLocationRelativeTo(owner);
        LoggerFactory.getLogger(PageSelectorDialogue.class).debug("Dialogue created [{}x{}]", getWidth(), getHeight());

        ok.addActionListener(e -> {
            result = new boolean[pages.length];
            for (int i = 0; i < pages.length; i++) result[i] = pages[i].isSelected();
            setVisible(false);
            dispose();
        });
        cancel.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
    }

    private PageSelectorElement[] initPages(String file) {  //todo add worker here
        BufferedImage[] imgs = FileIOController.getPdfPreviews(file);
        PageSelectorElement[] elements = new PageSelectorElement[imgs.length];
        for (int i = 0; i < imgs.length; i++) {
            elements[i] = new PageSelectorElement(imgs[i]);
        }
        return elements;
    }

    public boolean[] showDialog() {
        setVisible(true);
        return result;
    }
}
