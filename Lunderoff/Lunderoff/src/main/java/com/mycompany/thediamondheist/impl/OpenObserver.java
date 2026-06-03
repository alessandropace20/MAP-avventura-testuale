package com.mycompany.thediamondheist.impl;

import com.mycompany.thediamondheist.parser.ParserOutput;
import com.mycompany.thediamondheist.type.*;

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

        if (name.equals("terminale") && currentRoom == game.getSalaSorveglianza()) {
            if (!game.isCamerasEnabled()) {
                System.out.println("Hai già disattivato le telecamere, non serve riaprire il terminale.");
                return;
            }

            boolean hasKeys = currentPlayer.getInventory().containsByName("Chiavi Edificio");
            if (!hasKeys) {
                System.out.println("");
                return;
            }
            game.setCameras(hasKeys);

            if (findInRoom(currentRoom, "codice") == null) {
                AdvObject codice = new AdvObject(201, "codice", "Foglietto con una sequenza numerica.");
                codice.setPickupable(true);
                currentRoom.addObject(codice);
            }
            game.addScore(10);
            System.out.println("La serratura scatta: nel doppio fondo trovi un foglietto con un codice.");
            return;
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