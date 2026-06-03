/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.ui.GameController;
import code.yankton_bank.database.ScoreboardDAO;

import javax.swing.JOptionPane;

/**
 * Gestisce la chiusura della partita.
 * - Se invocato da comando "fine", chiude manualmente.
 * - Può essere richiamato in automatico (es. da MoveObserver) con finish(...).
 */
public class EndObserver implements GameObserver {

    
    public static void finish(GameDesc game, boolean victoryMsg) {
        if (game == null) return;

        int finalScore = game.getScore();

        Player pl = game.getPlayer();
        StringBuilder sb = new StringBuilder();
        sb.append("\n==================== FINE PARTITA ====================\n");
        sb.append(victoryMsg
                ? "Rapina riuscita! Hai recuperato l'oro e sei uscito dall'edificio.\n"
                : "Partita terminata.\n");
        
        sb.append("Punteggio finale: ").append(finalScore).append("\n");
        sb.append("======================================================\n");
        System.out.println(sb.toString());

        String defaultName = (pl != null && pl.getName() != null && !pl.getName().isBlank())
                ? pl.getName() : "Player";
        String name = JOptionPane.showInputDialog(
                null,
                "Inserisci il tuo nome per salvare il punteggio:",
                defaultName
        );
        if (name == null || name.isBlank()) name = defaultName;
        if (pl != null) pl.setName(name);

        try {
            new ScoreboardDAO().append(name, finalScore);
            System.out.println("Punteggio salvato nella scoreboard.");
        } catch (Exception e) {
            System.out.println("Impossibile salvare il punteggio: " + e.getMessage());
        }

        System.out.println("Grazie per aver giocato!");
        GameController.returnToMenu();
    }

    @Override
    public void update(ParserOutput p, Object g) {
        if (p == null || p.getCommand() == null) return;
        if (p.getCommand().getType() != CommandType.END) return;
        if (!(g instanceof GameDesc game)) return;

        finish(game, false);
    }
}
