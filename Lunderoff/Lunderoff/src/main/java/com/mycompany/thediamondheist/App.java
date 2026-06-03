package com.mycompany.thediamondheist;

import com.mycompany.thediamondheist.ui.MainMenuFrame;

/**
 * Main del progetto.
 */

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MainMenuFrame().setVisible(true));
    }
}
