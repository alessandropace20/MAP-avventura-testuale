package com.mycompany.thediamondheist.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * Finestra grafica semplificata per The Diamond Heist.
 * - Variante minimale di GameFrame, senza overlay o controlli extra
 * - Mostra l’immagine della stanza corrente e la console testuale
 * - Fornisce un campo input per i comandi del giocatore
 *
 * Utilizzata per test o versioni leggere del gioco, mantiene le stesse API
 * principali di GameFrame per compatibilità con GameController.
 */


public class SimpleGameFrame extends JFrame {
    private final JTextArea output = new JTextArea();
    private final JTextField input = new JTextField();
    private final JButton sendBtn = new JButton("Invia");

    private Consumer<String> onSubmit = s -> {};

    public SimpleGameFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 480);
        setLocationRelativeTo(null);

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        var scroll = new JScrollPane(output);
        var bottom = new JPanel(new BorderLayout(6, 6));
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);

        var root = new JPanel(new BorderLayout(6, 6));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        root.add(scroll, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);

        input.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) submit();
            }
        });
        sendBtn.addActionListener(e -> submit());
    }

    public void setOnSubmit(Consumer<String> onSubmit) {
        this.onSubmit = onSubmit == null ? s -> {} : onSubmit;
    }

    private void submit() {
        String txt = input.getText();
        input.setText("");
        if (txt != null && !txt.trim().isEmpty()) onSubmit.accept(txt);
    }

    public void println(String s) {
        if (s == null) s = "";
        output.append(s);
        output.append("\n");
        output.setCaretPosition(output.getDocument().getLength());
    }
}
