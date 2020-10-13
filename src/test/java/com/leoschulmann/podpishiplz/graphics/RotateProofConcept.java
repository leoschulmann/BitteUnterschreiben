package com.leoschulmann.podpishiplz.graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RotateProofConcept extends JFrame {

    public static void main(String[] args) throws IOException {
        JFrame r = new JFrame();
        r.setSize(500, 500);
        r.setResizable(false);
        r.setTitle("test frame");
        r.setDefaultCloseOperation(EXIT_ON_CLOSE);

        BufferedImage im = ImageIO.read(RotateProofConcept.class.getClassLoader().getResource("pholder.png"));
        CustomPanel pan = new CustomPanel(im, new Point(200, 200));
        r.add(pan);
        r.setLocationRelativeTo(null);
        r.setVisible(true);

        MouseAdapter ma = new mouse(pan);
        pan.addMouseListener(ma);
        pan.addMouseMotionListener(ma);
    }
}

class mouse extends MouseAdapter {
    private final CustomPanel panel;
    private double beginAngle;

    mouse(CustomPanel pan) {
        panel = pan;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        beginAngle = getAngle(panel.center.x, panel.center.y, e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double angle = getAngle(panel.center.x, panel.center.y, e.getX(), e.getY()) - beginAngle;

        panel.setRads(angle);
        panel.repaint();
    }

    private static double getAngle(int beginx, int beginy, int endx, int endy) {
        double rad = Math.atan2(endx - beginx, beginy - endy);
        if (rad < 0) {
            rad += Math.PI * 2;
        }
        return rad;
    }
}

class CustomPanel extends JPanel {
    private final BufferedImage im;
    private final Point topLeftCorner;
    Point center;
    private double rads = 0;


    CustomPanel(BufferedImage im, Point center) {
        this.im = im;
        this.center = center;
        topLeftCorner = new Point(this.center.x - im.getWidth() / 2, this.center.y - im.getHeight() / 2);
    }

    void setRads(double angle) {
        this.rads = angle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        BufferedImage newim = rotate(im, rads);

        //bounds and cross
        graphics2D.setColor(Color.magenta);
        g.drawImage(newim, topLeftCorner.x, topLeftCorner.y, newim.getWidth(), newim.getHeight(), null);
        g.drawRect(topLeftCorner.x, topLeftCorner.y, newim.getWidth(), newim.getHeight());
        g.drawLine(topLeftCorner.x + newim.getWidth() / 2, topLeftCorner.y, topLeftCorner.x + newim.getWidth() / 2, newim.getHeight() + topLeftCorner.y);
        g.drawLine(topLeftCorner.x, topLeftCorner.y + newim.getHeight() / 2, topLeftCorner.x + newim.getWidth(), topLeftCorner.y + newim.getHeight() / 2);
    }


    private BufferedImage rotate(BufferedImage im, double angle) {

//        double sin = Math.abs(Math.sin(angle));
//        double cos = Math.abs(Math.cos(angle));
//        int newWidth = (int) (im.getWidth() * cos + im.getHeight() * sin);
//        int newHeight = (int) (im.getHeight() * cos + im.getWidth() * sin);

        int maxDimension = (int) Math.sqrt(Math.pow(im.getWidth(), 2) + Math.pow(im.getHeight(), 2));
        BufferedImage rotated = new BufferedImage(maxDimension, maxDimension, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        int anchorX = maxDimension / 2;
        int anchorY = maxDimension / 2;
        int translateX = (maxDimension - im.getWidth()) / 2;
        int translateY = (maxDimension - im.getHeight()) / 2;

        at.rotate(angle, anchorX, anchorY);
        at.translate(translateX, translateY);
        g.setTransform(at);
        g.drawImage(im, 0, 0, null);
        g.dispose();
//        System.out.printf("max [%d,%d] this [%d,%d] new [%d,%d] rotc[%d,%d] trans[%d,%d]\n",
//                maxDimension, maxDimension, im.getWidth(), im.getHeight(), newWidth, newHeight,
//                anchorX, anchorY, translateX, translateY);

        return rotated;
    }
}

