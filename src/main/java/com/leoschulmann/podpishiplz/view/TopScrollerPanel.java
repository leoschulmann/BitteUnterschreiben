package com.leoschulmann.podpishiplz.view;

import com.leoschulmann.podpishiplz.controller.GUIController;
import com.leoschulmann.podpishiplz.controller.PDFController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TopScrollerPanel {
    private final JPanel panel;
    private final JScrollPane wrapper;

    public TopScrollerPanel() {
        panel = new JPanel(new FlowLayout());
        wrapper = new JScrollPane(panel);
        wrapper.setPreferredSize(new Dimension(100, 120));
    }

    public void loadFile(String file) throws IOException {
        BufferedImage[] images = PDFController.loadPDF(file);
        for (int i = 0; i < images.length; i++) {
            BufferedImage image = images[i];
            JButton jb = new JButton(new ImageIcon(image));
            int page = i;
            jb.addActionListener(e -> {
                try {
                    BufferedImage selectedPage300dpi = PDFController.get300dpiPage(file, page);
                    GUIController.openPage(selectedPage300dpi);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(this.getWrapper(), e.getClass().toString()
                            + " " + ioException.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                    ioException.printStackTrace();
                }
            });
            panel.add(jb);
        }
        panel.revalidate();
        panel.repaint();
    }

    public JScrollPane getWrapper() {
        return wrapper;
    }
}
