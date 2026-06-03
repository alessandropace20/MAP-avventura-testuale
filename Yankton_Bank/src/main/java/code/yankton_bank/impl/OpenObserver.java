/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;

/**
 * Observer che gestisce i comandi di apertura.
 * - Permette di aprire porte o oggetti
 * - Può sbloccare nuove aree del gioco
 */


public class OpenObserver implements GameObserver {

    @Override
    public void update(ParserOutput parser, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (parser.getCommand() == null || parser.getCommand().getType() != CommandType.OPEN) return;

        AdvObject currentObj = parser.getObject();
        Player currentPlayer = game.getPlayer();
        Room currentRoom = currentPlayer.getCurrentRoom();

        if (currentObj == null) {
            System.out.println("Aprire cosa?");
            return;
        }

        String name = safeName(currentObj);

        if (name.equals("armadietto") && currentRoom == game.getStanzinoPulizie()) {
            if (game.isLockerOpen()) {
                System.out.println("Hai già aperto l'armadietto. C'erano le chiavi dell'edificio.");
                return;
            }
            else{
                game.setLocker(true);
                AdvObject keys = findInRoom(currentRoom, "Chiavi");
                if (keys != null) {
                    keys.setVisible(true);
                }
                game.addScore(5);
                System.out.println("Forzandolo, riesci ad aprire l'armadietto: dentro trovi un mazzo di chiavi.");
                return;
            }
        }

        System.out.println("Non sembra che '" + currentObj.getName() + "' si possa aprire ora.");
    }


    private static String safeName(AdvObject obj) {
        String n = (obj == null || obj.getName() == null) ? "" : obj.getName();
        return n.trim().toLowerCase();
    }

    private static AdvObject findInRoom(Room r, String name) {
        if (r == null || name == null) return null;
        String n = name.trim().toLowerCase();
        for (AdvObject obj : r.getObjects()) {
            if (obj != null && obj.getName() != null && obj.getName().equalsIgnoreCase(n)) return obj;
        }
        return null;
    }
}