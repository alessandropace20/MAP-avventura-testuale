package com.mycompany.thediamondheist.impl;

import com.mycompany.thediamondheist.parser.ParserOutput;
import com.mycompany.thediamondheist.type.*;

/**
 * Observer che gestisce i comandi di osservazione.
 * - Permette di esaminare stanze, oggetti o l’ambiente circostante
 */


public class LookObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.LOOK) return;

        Room cur = game.getPlayer().getCurrentRoom();
        System.out.println("== " + cur.getName() + " ==");
        System.out.println(cur.getDescription());

        if (!cur.getObjects().isEmpty()) {
            System.out.println("Vedi:");
            for (AdvObject o : cur.getObjects()) {
                if (o.isVisible()) {
                    System.out.println("- " + o.getName());
                }
            }
        }
    }
}
