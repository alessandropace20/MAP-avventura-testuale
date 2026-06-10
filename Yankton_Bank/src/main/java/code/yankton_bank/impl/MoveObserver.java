/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import java.util.function.Supplier;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.PrettyPrint;

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

        String result = ((Supplier<String>) () -> {
            StringBuilder sb = new StringBuilder();

            if (next == null) {
                sb.append("Non puoi andare in quella direzione.");
                return sb.toString();
            }
            if (next.isLocked()) {
                sb.append("La stanza è bloccata. E' necessario sbloccare l'ingresso.");
                return sb.toString();
            } else {
                pl.setCurrentRoom(next);
                sb.append("Ti sposti in: ").append(next.getName()).append(".");

                if (next.getDescription() != null && !next.getDescription().isBlank()) {
                    sb.append("\n").append(next.getDescription());
                }
            }

            return sb.toString();
        }).get();

        System.out.println(PrettyPrint.print(result));

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
