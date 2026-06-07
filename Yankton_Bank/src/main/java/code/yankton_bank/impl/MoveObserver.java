/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.Concurrent;

/**
 * Gestisce i movimenti del giocatore (nord/sud/est/ovest)
 * e TRIGGERA la fine automatica quando si entra all'Ingresso con l'oro.
 */
public class MoveObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (p == null || p.getCommand() == null) return;

        CommandType t = p.getCommand().getType();
        String dir = switch (t) {
            case NORTH -> "nord";
            case SOUTH -> "sud";
            case EAST  -> "est";
            case WEST  -> "ovest";
            default    -> null;
        };

        if (dir == null) return;

        Player pl = game.getPlayer();
        if (pl == null || pl.getCurrentRoom() == null) return;

        Room current = pl.getCurrentRoom();
        Room next = current.getExit(dir);

        if (next == null) {
            System.out.println("Non puoi andare in quella direzione.");
            return;
        }
        if (next.isLocked()){
            System.out.println("La stanza è bloccata. E' necessario sbloccare l'ingresso.");
            return;
        }
        else {
            pl.setCurrentRoom(next);
            System.out.println("Ti sposti in: " + next.getName() + ".");
            if (next.getDescription() != null && !next.getDescription().isBlank()) {
                System.out.println(next.getDescription());
            }
            if (next.getName() == "Corridoio" && game.isSecurityEnabled()) {
                System.out.println("\n======================================================\n");
                    System.out.println("Diamine! Hai fatto scattare l'allarme!\n\n" + 
                        "GAME OVER");
                    System.out.println("\n======================================================\n");                  
                    Concurrent.runAsync(() -> {
                        try {
                            Thread.sleep(2000);
                            EndObserver.finish(game, false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
            }
        }

        try {
            boolean exitPoint = (next.getId() == 1);
            boolean hasGold = pl.getInventory().containsByName("oro");

            if (exitPoint && hasGold) {
                EndObserver.finish(game, true); 
                return;
            }
        } catch (Exception ignore) {
        }
    }
}
