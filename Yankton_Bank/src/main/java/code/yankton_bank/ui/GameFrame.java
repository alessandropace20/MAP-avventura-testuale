/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Finestra principale della GUI di Yankton Bank.
 * - Mostra immagine della stanza corrente
 * - Contiene console testuale e campo input per i comandi
 * - Gestisce pulsanti aggiuntivi (Salva, Carica, Esci)
 */


public class GameFrame extends JFrame {

    private final JTextArea console = new JTextArea();
    private final JTextField input = new JTextField();
    private final JButton sendBtn = new JButton("Invia");
    private final JLabel imageLabel = new JLabel();     
    private static final double IMAGE_OVERSCALE = 1.90;

    private final GameController controller;
    private int currentRoomId = -1;

    public GameFrame(GameController controller) {
        super("Yankton Bank");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.BLACK);

        console.setEditable(false);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        console.setBackground(Color.BLACK);     // sfondo
        console.setForeground(Color.WHITE);     // testo
        console.setCaretColor(Color.WHITE);     // cursore
        console.setLineWrap(true);
        console.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(console);

        JPanel bottom = new JPanel(new BorderLayout(6, 6));
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        JPanel right = new JPanel(new BorderLayout(6, 6));
        right.add(scroll, BorderLayout.CENTER);
        right.add(bottom, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, imageLabel, right);
        split.setResizeWeight(0.30);

        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        topButtons.setOpaque(false);

        JButton btnSave = new JButton("Salva");
        btnSave.addActionListener(e -> controller.saveLastToFile());

        JButton btnExit = new JButton("Esci");
        btnExit.addActionListener(e -> {
            GameController.returnToMenu();
            dispose();
        });

        topButtons.add(btnSave);
        topButtons.add(btnExit);

        JPanel root = new JPanel(new BorderLayout());
        root.add(topButtons, BorderLayout.NORTH);
        root.add(split, BorderLayout.CENTER);
        setContentPane(root);

        sendBtn.addActionListener(e -> submit());
        input.addActionListener(e -> submit());

        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                if (currentRoomId > 0) setRoomImageById(currentRoomId);
            }
        });
    }


    public void println(String text) {
        console.append(text + "\n");
        console.setCaretPosition(console.getDocument().getLength());
    }

    public void setStatus(String text) {
        setTitle("Yankton Bank — " + text);
    }

    public void setRoomImageById(int roomId) {
        this.currentRoomId = roomId;
        String fileName = getImageNameForRoom(roomId);
        if (fileName == null) {
            imageLabel.setIcon(null);
            imageLabel.setText("[Nessuna immagine]");
            return;
        }
        drawFramedImage("/images/" + fileName);
    }


    private void submit() {
        String cmd = input.getText().trim();
        if (!cmd.isEmpty()) {
            println("> " + cmd);
            input.setText("");
            controller.submit(cmd);
        }
    }

    private String getImageNameForRoom(int roomId) {
        return switch (roomId) {
            case 0 -> "menu.png";                   // menu
            case 1 -> "ingresso.png";               // ingresso
            case 2 -> "stanzino.png";               // stanzino
            case 3 -> "salaSorveglianza.png";       // sorveglianza
            case 4 -> "corridoio.png";               // corridoio
            case 5 -> "ingCaveau.png";                   // ingresso del caveau
            case 6 -> "caveau.png";                   // caveau con tesoro
            case 8 -> "uscita.png";                   // uscita
            default -> null;
        };
    }

    
    private void drawFramedImage(String classpathImage) {
        try {
            BufferedImage original = ImageIO.read(getClass().getResource(classpathImage));
            if (original == null) {
                imageLabel.setIcon(null);
                imageLabel.setText("[Immagine non trovata]");
                return;
            }

            int panelW = Math.max(1, imageLabel.getWidth());
            int panelH = Math.max(1, imageLabel.getHeight());

            double scale = Math.min((double) panelW / original.getWidth(),
                                    (double) panelH / original.getHeight());

            scale *= IMAGE_OVERSCALE;

            int newW = (int) Math.round(original.getWidth() * scale);
            int newH = (int) Math.round(original.getHeight() * scale);

            BufferedImage framed = new BufferedImage(panelW, panelH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = framed.createGraphics();
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, panelW, panelH);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int x = (panelW - newW) / 2;
            int y = (panelH - newH) / 2;
            g2d.drawImage(original, x, y, newW, newH, null);
            g2d.dispose();

            imageLabel.setIcon(new ImageIcon(framed));
            imageLabel.setText(null);

        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("[Errore immagine]");
        }
    }
}

