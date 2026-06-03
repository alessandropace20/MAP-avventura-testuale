/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
/**
 * Pannello console: input dell’utente e rendering dell’output testuale.
 * Notifica il GameController alla pressione di Invio.
 */
public class ConsolePanel extends JPanel {
    private final JTextArea output;
    private final JTextField input;
    private Consumer<String> onSubmit = null;

    public ConsolePanel() {
        super(new BorderLayout(0, 6));

        output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(output);
        add(sp, BorderLayout.CENTER);

        
        JPanel south = new JPanel(new BorderLayout(6, 0));
        input = new JTextField();
        JButton send = new JButton("Invia");
        south.add(input, BorderLayout.CENTER);
        south.add(send, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);

        
        input.addActionListener(e -> fireSubmit());
        
        send.addActionListener(e -> fireSubmit());
    }

    private void fireSubmit() {
        String txt = input.getText() == null ? "" : input.getText().trim();
        if (txt.isEmpty()) return;
        if (onSubmit != null) onSubmit.accept(txt);
        input.setText("");
    }

    
    public void setOnSubmit(Consumer<String> onSubmit) {
        this.onSubmit = onSubmit;
    }

   
    public void appendLine(String s) {
        if (s == null) return;
        output.append(s);
        output.append("\n");
        output.setCaretPosition(output.getDocument().getLength());
    }
}

