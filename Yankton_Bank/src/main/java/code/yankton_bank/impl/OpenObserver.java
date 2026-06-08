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

        String result = ((Supplier<String>) () -> {
            StringBuilder sb = new StringBuilder();
        
            switch (name) {
                case "porta" -> {
                    if (currentRoom == game.getIngresso()) {
                        if (!game.getStanzinoPulizie().isLocked()) {
                            sb.append("La porta è già aperta.");
                        } else {
                            sb.append("La porta d'ingresso è, chiaramente, bloccata.")
                              .append("\nControlla nel tuo inventario se hai qualcosa per scassinarla. Assicurati di farlo in sicurezza.");
                        }
                        return sb.toString();
                    }
                    if (currentRoom == game.getStanzinoPulizie()) {
                        if (!game.getSalaSorveglianza().isLocked()) {
                            sb.append("La porta è già aperta.");
                        } else {
                            sb.append("La porta è bloccata. Sembra essere chiusa a chiave, forse c'è qualcosa che puoi usare per aprirla.");
                        }
                        return sb.toString();
                    }
                    if (currentRoom == game.getIngCaveau()) {
                        if (game.isSecurityEnabled()) {
                            sb.append("La porta blindata è bloccata elettronicamente.\n")
                              .append("Non sembra esserci un modo senza disattivare le misure di sicurezza.");
                        } else {
                            sb.append("Essendo che hai disattivato le misure di sicurezza, la porta blindata si apre senza problemi.\n")
                              .append("Da ora conviene non fare stupidaggini, trova la riserva d'oro e fuggi dall'edificio.");
                        }
                    }
                }
                case "armadietto" -> {
                    if (currentRoom == game.getStanzinoPulizie()) {
                        if (game.isLockerOpen()) {
                            sb.append("Hai già aperto l'armadietto. C'era quel mazzo di chiavi.");
                        } else {
                            game.setLocker(true);
                            AdvObject keys = findInRoom(currentRoom, "Chiavi");
                            keys.setVisible(true);
                            game.addScore(5);
                            sb.append("Forzandolo, riesci ad aprire l'armadietto: dentro trovi un mazzo di chiavi.");
                        }
                    }
                }
                case "cassetta" -> {
                    if (currentRoom == game.getCaveau()) {
                        sb.append("Apri la cassetta di sicurezza e trovi la riserva d'oro del trafficante. Prendilo e scappa!");
                        AdvObject.findInRoom(currentRoom, "Oro").setVisible(true);
                    }
                }
                default -> sb.append("Temo che < ").append(currentObj.getName()).append(" > non si possa aprire qui.");
            }
        
            return sb.toString();
        }).get();
        System.out.println(PrettyPrint.print(result));
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