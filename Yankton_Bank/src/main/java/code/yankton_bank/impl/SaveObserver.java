/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.database.GameSaveDAO;

import javax.swing.JOptionPane;

/**
 * Observer che gestisce l'azione di salvataggio.
 * - Breve descrizione del comportamento
 * - Effetti collaterali sullo stato di gioco/UI (se necessari)
 */


public class SaveObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (p == null || p.getCommand() == null) return;

        CommandType type = p.getCommand().getType();

        try {
            if (type == CommandType.SAVE) {
                new GameSaveDAO().save(game, "slot1");
                System.out.println("Partita salvata (slot1).");
                try { JOptionPane.showMessageDialog(null, "Partita salvata (slot1)."); } catch (Exception ignored) {}
            } else if (type == CommandType.LOAD) {
                new GameSaveDAO().loadInto(game, "slot1");
                System.out.println("Partita caricata (slot1).");
                try { JOptionPane.showMessageDialog(null, "Partita caricata (slot1)."); } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.out.println("Errore durante il salvataggio/caricamento: " + e.getMessage());
            try { JOptionPane.showMessageDialog(null, "Errore: " + e.getMessage()); } catch (Exception ignored) {}
        }
    }
}
