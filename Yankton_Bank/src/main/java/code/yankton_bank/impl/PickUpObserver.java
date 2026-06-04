/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.MusicHandler; 

/**
 * Observer che gestisce i comandi di raccolta.
 * - Permette di prendere oggetti presenti nella stanza
 * - Aggiunge gli oggetti all’inventario del player
 */


public class PickUpObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.PICK_UP) return;

        AdvObject obj = p.getObject();
        Player pl = game.getPlayer();
        Room cur = pl.getCurrentRoom();

        if (obj == null) {
            System.out.println("Cosa dovresti prendere?");
            return;
        }

        String name = safeName(obj);

        AdvObject inRoom = findInRoom(cur, name);
        if (inRoom == null || !inRoom.isVisible()) {
            System.out.println("Qui non vedo '" + obj.getName() + "'.");
            return;
        }
        if (!inRoom.isPickupable()) {
            System.out.println("Non puoi prendere '" + obj.getName() + "'.");
            return;
        }

        removeFromRoom(cur, name);
        pl.getInventory().add(inRoom);
        System.out.println("Raccolto: " + inRoom.getName());
        if(name == "oro") {
            System.out.println("Hai raccolto l'oro, torna indietro e fuggi dall'edificio!");
        }
        MusicHandler.playSfx("/audio/pickup.wav"); // [AUDIO]


        if (name.equals("chiavi") && cur == game.getStanzinoPulizie()) {
                game.addScore(10); //sostituire con il timer in concorrenza: aggiungere minuti al tempo rimanente, etc.
                System.out.println("Essenziali. Accelerano di gran lunga il piano, ottimo.");
        }
    }


    private static String safeName(AdvObject o) {
        String n = (o == null || o.getName() == null) ? "" : o.getName();
        return n.trim().toLowerCase();
    }

    private static AdvObject findInRoom(Room r, String name) {
        if (r == null || name == null) return null;
        String n = name.trim().toLowerCase();
        for (AdvObject o : r.getObjects()) {
            if (o != null && o.getName() != null && o.getName().equalsIgnoreCase(n)) {
                return o;
            }
        }
        return null;
    }

    private static void removeFromRoom(Room r, String name) {
        if (r == null || name == null) return;
        String n = name.trim().toLowerCase();
        var it = r.getObjects().iterator();
        while (it.hasNext()) {
            AdvObject o = it.next();
            if (o != null && o.getName() != null && o.getName().equalsIgnoreCase(n)) {
                it.remove();
                return;
            }
        }
    }
}
