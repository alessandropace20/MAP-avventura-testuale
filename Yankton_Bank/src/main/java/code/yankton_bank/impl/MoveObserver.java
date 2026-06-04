/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;

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

        if(next == null) {
            System.out.println("Non puoi andare in quella direzione.");
            return;
        }
        if(next.isLocked()){
            System.out.println("La stanza è bloccata. E' necessario sbloccare l'ingresso.");
            return;
        }
        else {
            pl.setCurrentRoom(next);
            System.out.println("Ti sposti in: " + next.getName() + ".");
            //se la prossima è il caveau, implementare un dialogo per far capire che la porta blindata è aperta data la disativazione dei sistemi di sicurezza
            if (next.getDescription() != null && !next.getDescription().isBlank()) {
                System.out.println(next.getDescription());
            }
        }

        //MusicHandler.playSfx("/audio/footsteps.wav");

        if (next != null) {
            int id = next.getId();
            if (id == 1 || id == 3 || id == 5 || id == 7) {
                //MusicHandler.playSfx("/audio/door_open.wav");
            }
        }


        try {
            boolean atEntrance = (next.getId() == 1);
            boolean hasGold = pl.getInventory().containsByName("oro");

            if (atEntrance && hasGold) {
                EndObserver.finish(game, true); 
                return; 
            }
        } catch (Exception ignore) {
        }
    }
}
