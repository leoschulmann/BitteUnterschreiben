package com.leoschulmann.podpishiplz.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel {
    private BufferedImage bufIm;

    @Override
    protected void paintComponent(Graphics g) {
        int newHeight = 0;
        int newWidth = 0;
        if (bufIm != null) {
            if (bufIm.getHeight() > bufIm.getWidth()) { //picture is vertical
                newHeight = this.getHeight() - 20;
                newWidth = newHeight * bufIm.getWidth() / bufIm.getHeight();
                g.drawImage(bufIm, (this.getWidth() - newWidth) / 2, 10, newWidth, newHeight, null);
            } else {
                newWidth = this.getWidth() - 20;
                newHeight = newWidth * bufIm.getHeight() / bufIm.getWidth();
                g.drawImage(bufIm, 10, (this.getHeight() - newHeight) / 2, newWidth, newHeight, null);

            }
        }
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufIm = bufferedImage;
    }
}
