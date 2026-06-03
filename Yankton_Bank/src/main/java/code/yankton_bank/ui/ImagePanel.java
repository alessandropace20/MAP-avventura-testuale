/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.ui;

import javax.swing.*;
import java.awt.*;

/** Pannello per mostrare l'immagine della stanza con overlay scuro. */
public class ImagePanel extends JLayeredPane {
    private final JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel overlay = new JPanel();
    private boolean dark = true;

    public ImagePanel() {
        setLayout(null);
        setPreferredSize(new Dimension(380, 0));

        imageLabel.setBounds(0,0, getPreferredSize().width, getPreferredSize().height);
        add(imageLabel, Integer.valueOf(0));

        overlay.setOpaque(true);
        overlay.setBackground(new Color(0,0,0,180));
        overlay.setBounds(0,0, getPreferredSize().width, getPreferredSize().height);
        add(overlay, Integer.valueOf(1));

        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        updateOverlay();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        imageLabel.setBounds(0,0, getWidth(), getHeight());
        overlay.setBounds(0,0, getWidth(), getHeight());
    }

    
    public void setImageIcon(Icon icon) {
        imageLabel.setIcon(icon);
        revalidate();
        repaint();
    }

    
    public void setDark(boolean dark) {
        this.dark = dark;
        updateOverlay();
    }

    private void updateOverlay() {
        overlay.setVisible(dark);
        overlay.repaint();
    }
}

