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
 * Observer che gestisce i comandi di raccolta.
 * - Permette di prendere oggetti presenti nella stanza
 * - Aggiunge gli oggetti all’inventario del player
 */


public class PickUpObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game))
            return;
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.PICK_UP)
            return;

        AdvObject obj = p.getObject();
        Player pl = game.getPlayer();
        Room cur = pl.getCurrentRoom();

        if (obj == null) {
            System.out.println("Cosa dovresti prendere?");
            return;
        }

        String name = safeName(obj);

        AdvObject inRoom = findInRoom(cur, name);
        String result = ((Supplier<String>) () -> {
            StringBuilder sb = new StringBuilder();

            if (inRoom == null || !inRoom.isVisible()) {
                sb.append("Qui non vedo '").append(obj.getName()).append("'.");
                return sb.toString();
            }
            if (!inRoom.isPickupable()) {
                sb.append("Non puoi prendere '").append(obj.getName()).append("'.");
                return sb.toString();
            }

            removeFromRoom(cur, name);
            pl.getInventory().add(inRoom);
            sb.append("Raccolto: ").append(inRoom.getName());

            if (name.equals("oro")) {
                sb.append("\nHai raccolto l'oro, torna indietro e fuggi dall'edificio!");
            }

            if (name.equals("chiavi") && cur == game.getStanzinoPulizie()) {
                game.addScore(10);
                sb.append("\nEssenziali. Accelerano di gran lunga il piano, ottimo.");
            }

            return sb.toString();
        }).get();
        System.out.println(PrettyPrint.print(result));
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
