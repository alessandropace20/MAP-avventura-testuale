/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;

/**
 * Observer che gestisce i comandi di osservazione.
 * - Permette di esaminare stanze, oggetti o l’ambiente circostante
 */


public class LookObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.LOOK) return;
        System.out.println("\n======================================================\n");
        Room cur = game.getPlayer().getCurrentRoom();
        
        System.out.println("Ti trovi in: " + cur.getName() + "\n");
        System.out.println(cur.getDescription());

        if (!cur.getObjects().isEmpty()) {
            System.out.println("Vedi:");
            for (AdvObject o : cur.getObjects()) {
                if (o.isVisible()) {
                    System.out.println("- " + o.getName());
                }
            }
        }
        System.out.println("\n======================================================\n");
    }
}
