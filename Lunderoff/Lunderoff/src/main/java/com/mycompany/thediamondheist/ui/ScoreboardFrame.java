package com.mycompany.thediamondheist.ui;

import com.mycompany.thediamondheist.database.ScoreboardDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Finestra per mostrare la classifica (Scoreboard).
 */
public class ScoreboardFrame extends JFrame {

    public ScoreboardFrame() {
        setTitle("Classifica - The Diamond Heist");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] columns = {"Giocatore", "Punteggio"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        ScoreboardDAO dao = new ScoreboardDAO();
        List<String[]> scores = dao.loadAll();
        for (String[] s : scores) {
            model.addRow(new Object[]{s[0], s[1]});
        }

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);
    }
}
