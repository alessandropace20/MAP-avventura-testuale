/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.ui;

import javax.swing.*;
import java.awt.*;
import code.yankton_bank.util.MusicHandler; 

/**
 * Finestra del menu principale.
 * - Mostra le opzioni iniziali (Nuova Partita, Carica Ultimo Salvataggio, Comandi, Scoreboard, Esci)
 * - Gestisce la creazione del GameController e l’avvio del gioco
 */
public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        super("Yankton Bank");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(18, 20, 26));
        setContentPane(root);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Yankton Bank");
        title.setForeground(new Color(200, 180, 180));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 36f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 0;
        root.add(title, c);

        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(10, 20));
        c.gridy = 1;
        root.add(spacer, c);

        JButton btnNew   = makeButton("Nuova Partita");
        btnNew.setForeground(new Color(80, 200, 120));
        JButton btnLoad  = makeButton("Carica Salvataggio Esistente");
        btnLoad.setForeground(new Color(50, 50, 50));
        JButton btnHelp  = makeButton("Comandi");
        btnHelp.setForeground(new Color(50, 50, 50));
        JButton btnScore = makeButton("Scoreboard");
        btnScore.setForeground(new Color(50, 50, 50));
        JButton btnExit  = makeButton("Esci");
        btnExit.setForeground(new Color(128, 0, 32));

        c.gridy = 2; root.add(btnNew,   c);
        c.gridy = 3; root.add(btnLoad,  c);
        c.gridy = 4; root.add(btnHelp,  c);
        c.gridy = 5; root.add(btnScore, c);
        c.gridy = 6; root.add(btnExit,  c);

        btnNew.addActionListener(e -> {
            MusicHandler.stopLoop();

            new GameController(true); // mostra intro
            dispose();
        });

        btnLoad.addActionListener(e -> {
            MusicHandler.stopLoop();

            GameController gc = new GameController(false);
            boolean ok = gc.loadLastFromFile();
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Nessun salvataggio trovato.\nParte una nuova partita.",
                        "Caricamento", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        });

        btnHelp.addActionListener(e -> {
            String help = """
                    Comandi disponibili:
                      - vai nord/sud/est/ovest
                      - guarda
                      - prendi <oggetto>
                      - usa <oggetto>
                      - apri <oggetto>
                      - inventario
                      - aiuto
                      - fine
                    """;
            JOptionPane.showMessageDialog(this, help, "Comandi", JOptionPane.PLAIN_MESSAGE);
        });

        btnScore.addActionListener(e -> {
            new ScoreboardFrame().setVisible(true);
        });

        btnExit.addActionListener(e -> {
            MusicHandler.stopLoop();
            System.exit(0);
        });

    /*MusicHandler.playLoop("/audio/menu.wav");
    MusicHandler.preload(
    "/audio/door_open.wav",
    "/audio/footsteps.wav",
    "/audio/pickup.wav",
    "/audio/switch.wav"
    );*/

    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(54, 61, 76));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 18f));
        b.setPreferredSize(new Dimension(420, 52));
        return b;
    }
}
