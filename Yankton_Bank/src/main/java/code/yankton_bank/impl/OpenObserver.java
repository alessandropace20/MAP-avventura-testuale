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

        if (currentObj == null || !currentObj.isVisible()) {
            System.out.println("Aprire cosa?");
            return;
        }

        String name = safeName(currentObj);

        if (name.equals("porta") && currentRoom == game.getIngresso()) {
            if (game.isLockerOpen()) {
                System.out.println("La porta è già aperta.");
                return;
            }
            else{
                System.out.println("La porta è bloccata. Forse c'è un modo per sbloccarla.");
                return;
            }
        }

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

        if (name.equals("blindata")) {
            if (currentRoom == game.getCaveau()) {
                if (game.isSecurityEnabled()) {
                    System.out.println("La porta blindata è bloccata elettronicamente.\n" +
                        "Non sembra esserci un modo senza disattivare le misure di sicurezza.");
                    return;
                }
                else {
                    System.out.println("Essendo che hai disattivato le misure di sicurezza, la porta blindata si apre senza problemi.\n"+
                        "Da ora conviene non fare stupidaggini, trova la riserva d'oro e fuggi dall'edificio.");
                    AdvObject.findInRoom(currentRoom, "cassetta").setVisible(true);
                    AdvObject.findInRoom(currentRoom, "oro").setVisible(true);
                    return;
                }
            }
        }

        if (name.equals("cassetta")) {
            if (currentRoom == game.getCaveau()) {
                
            }
        }

        System.out.println("Temo che < " + currentObj.getName() + " > non si possa aprire qui.");
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