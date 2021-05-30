package com.leoschulmann.podpishiplz.view;


import com.leoschulmann.podpishiplz.BitteUnterschreiben;
import com.leoschulmann.podpishiplz.controller.DnDListener;
import com.leoschulmann.podpishiplz.controller.MainPanelController;
import com.leoschulmann.podpishiplz.controller.SettingsController;
import com.leoschulmann.podpishiplz.graphics.Rotater;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

@Slf4j
public class MainPanel extends JPanel {
    @Getter
    private final JScrollPane mainPanelWrapper;
    private final Color bgColor;
    private final MouseController mouse;

    private Color dndColor;
    private int dndFrameInset;
    private String dropText;
    private Stroke dashStroke;
    private Font dropFont;

    @Setter
    private DrawMode mode = DrawMode.EMPTY;

    public MainPanel() {
        mainPanelWrapper = new JScrollPane(this);
        mainPanelWrapper.setBorder(BorderFactory.createEmptyBorder());
        bgColor = UIManager.getColor("Panel.background");
        MainPanelController.setMainPanel(this);

        mouse = new MouseController(this);
        mainPanelWrapper.addMouseListener(mouse);
        mainPanelWrapper.addMouseMotionListener(mouse);
        mainPanelWrapper.addMouseWheelListener(mouse);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });

        initDragNDropConstants();

        DropTarget dt = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DnDListener(), true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        switch (mode) {
            case PAGE:
                if (getMouseListeners().length == 0) addMouseListener(mouse);
                if (getMouseMotionListeners().length == 0) addMouseMotionListener(mouse);
                if (getMouseWheelListeners().length == 0) addMouseWheelListener(mouse);

                setPreferredSize(new Dimension(MainPanelController.getPanelWidth(), MainPanelController.getPanelHeight()));

                g.setColor(bgColor);
                g.fillRect(0, 0, MainPanelController.getPanelWidth(), MainPanelController.getPanelHeight());

                g.drawImage(MainPanelController.getImage(),
                        MainPanelController.getInsetX(),
                        MainPanelController.getInsetY(),
                        MainPanelController.getZoomedImageWidth(),
                        MainPanelController.getZoomedImageHeight(), null);

                g.setColor(Color.GRAY);
                g.drawRect(MainPanelController.getInsetX(),
                        MainPanelController.getInsetY(),
                        MainPanelController.getZoomedImageWidth(),
                        MainPanelController.getZoomedImageHeight());

                if (MainPanelController.getOverlays().size() > 0) {
                    MainPanelController.getOverlays().forEach(o -> {
                        BufferedImage image;
                        int overlayResizeWidth;
                        int overlayResizeHeight;
                        int overlayX = MainPanelController.getOverlayX(o);
                        int overlayY = MainPanelController.getOverlayY(o);
                        if (o.getRotation() != 0.) {
                            image = Rotater.freeRotate(o.getImage(), o.getRotation());
                            overlayResizeWidth = (int) (MainPanelController.getZoom() * image.getWidth());
                            overlayResizeHeight = (int) (MainPanelController.getZoom() * image.getHeight());

                        } else {
                            image = o.getImage();
                            overlayResizeWidth = MainPanelController.getOverlayResizeWidth(o);
                            overlayResizeHeight = MainPanelController.getOverlayResizeHeight(o);
                        }
                        o.setBounds(overlayX, overlayY, overlayResizeWidth, overlayResizeHeight);
                        g.drawImage(image, overlayX, overlayY, overlayResizeWidth, overlayResizeHeight, null);
                    });
                }

                MainPanelController.getSelectedOverlayBounds().ifPresent(bounds -> {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(Color.decode('#' + SettingsController.getSelectionColor()));
                    g2.setStroke(new BasicStroke(2));
                    if (MainPanelController.isRotatingMode()) {
                        g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
                    } else {
                        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
                    }

                });
                log.trace("DRAWING\tvp:[{},{}]\tp:[{},{}]\tim:[{},{}]",
                        mainPanelWrapper.getWidth(), mainPanelWrapper.getHeight(),
                        this.getWidth(), this.getHeight(), MainPanelController.getZoomedImageWidth(),
                        MainPanelController.getZoomedImageHeight());
                break;
            case EMPTY:
                removeMouseListener(mouse);
                removeMouseMotionListener(mouse);
                removeMouseWheelListener(mouse);
                BufferedImage im;
                try {
                    im = ImageIO.read(this.getClass().getClassLoader().getResource("splash.png")); //todo cache
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    JOptionPane.showMessageDialog(BitteUnterschreiben.getApp(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    im = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                    Graphics gr = im.createGraphics();
                    gr.setColor(Color.red);
                    gr.drawString("Error", 5, 50);
                    gr.dispose();
                }
                g.setColor(bgColor);
                g.fillRect(0, 0, getWidth(), getHeight());

                int imX0 = (mainPanelWrapper.getWidth() - im.getWidth()) / 2;
                int imY0 = (mainPanelWrapper.getHeight() - im.getHeight()) / 2;

                g.drawImage(im, imX0, imY0, im.getWidth(), im.getHeight(), null);
                setPreferredSize(new Dimension(im.getWidth(), im.getHeight()));
                mainPanelWrapper.getViewport().setViewPosition(new Point(0,0));
                break;

            case DND:
                removeMouseListener(mouse);
                removeMouseMotionListener(mouse);
                removeMouseWheelListener(mouse);

                g.setColor(bgColor);
                g.fillRect(0, 0, getWidth(), getHeight());

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(dashStroke);
                g2.setColor(dndColor);
                g2.drawRect(dndFrameInset, dndFrameInset, getWidth() - 2 * dndFrameInset, getHeight() - 2 * dndFrameInset);
                g2.setFont(dropFont);
                FontMetrics fontMetrics = g2.getFontMetrics(dropFont);
                int x = (getWidth() - fontMetrics.stringWidth(dropText)) / 2;
                int y = ((getHeight() - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();

                g2.drawString(dropText, x, y);
                break;
        }
    }

    private void initDragNDropConstants() {
        dndColor = Color.LIGHT_GRAY;
        dndFrameInset = 50;
        dashStroke = new BasicStroke(15, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND,
                0, new float[]{25, 30}, 0);
        dropText = "Drop .PDF here";
        dropFont = new Font(Font.SANS_SERIF, Font.BOLD, 64);
    }
}
