/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import java.util.function.Supplier;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.Concurrent;
import code.yankton_bank.util.PrettyPrint;

/**
 * Observer che gestisce i comandi di utilizzo.
 * - Permette di usare oggetti dell’inventario o presenti nella stanza
 * - Può attivare eventi o cambiare lo stato del gioco
 */
public class UseObserver implements GameObserver {

    @Override
    public void update(ParserOutput parser, Object g) {
        if (!(g instanceof GameDesc game))
            return;
        if (parser.getCommand() == null || parser.getCommand().getType() != CommandType.USE)
            return;

        AdvObject currentObj = parser.getObject();
        Player currentPlayer = game.getPlayer();
        Room currentRoom = currentPlayer.getCurrentRoom();

        if (currentObj == null) {
            System.out.println("Usare cosa?");
            return;
        }

        String name = safeName(currentObj);

        String result = ((Supplier<String>) () -> {
            StringBuilder sb = new StringBuilder();
        
            switch (name) {
        
                // 1.0 - Usare le pinze
                case "pinze" -> {
                    if (currentPlayer.getInventory().containsByName("Pinze")) {
                        if (currentRoom == game.getIngresso()) {
                            sb.append("Usi le pinze per tagliare i cavi elettrici esterni.\nTelecamera disabilitata.");
                            game.addScore(15);
                            game.setCamera(false);
                        }
                    } else {
                        sb.append("Non hai delle pinze.");
                    }
                }
        
                // 1.1 - Usare il grimaldello
                case "grimaldello" -> {
                    if (currentPlayer.getInventory().containsByName("Grimaldello")) {
                        if (currentRoom == game.getIngresso()) {
                            sb.append("Usi il grimaldello per forzare la porta.\nPorta nord sbloccata.");
                            game.getStanzinoPulizie().setLocked(false);
                            if (game.isCameraEnabled()) {
                                game.decreaseScore(10);
                            } else {
                                game.addScore(15);
                            }
                        }
                        if (currentRoom == game.getIngCaveau()) {
                            sb.append("Sul serio vuoi aprire una porta blindata da 2 quintali con un grimaldello?\n")
                              .append("Devi trovare un altro modo per aprirla.\n")
                              .append("Ti converrebbe disattivare i sistemi di sicurezza, anche se non mi sembri un tipo sveglio.");
                        }
                    } else {
                        sb.append("Non hai alcun grimaldello.");
                    }
                }
        
                // 2.0 - Usare le chiavi
                case "chiavi" -> {
                    if (currentPlayer.getInventory().containsByName("Chiavi")) {
                        if (currentRoom == game.getStanzinoPulizie()) {
                            if (game.getSalaSorveglianza().isLocked()) {
                                sb.append("Porta nord sbloccata.");
                                game.getSalaSorveglianza().setLocked(false);
                                game.addScore(15);
                            } else {
                                sb.append("Porta già sbloccata.");
                            }
                        }
                        if (currentRoom == game.getIngCaveau()) {
                            sb.append("Non credo che in un mazzo di chiavi trovato in uno stanzino delle pulizie, ci sia una chiave che possa aprire")
                              .append(" una porta blindata spessa più del tuo cervello, genio...");
                        }
                    } else {
                        sb.append("Non hai delle chiavi.");
                    }
                }
        
                // 2.1 - Usare il terminale principale
                case "terminale" -> {
                    if (currentRoom == game.getSalaSorveglianza()) {
                        if (game.isSecurityEnabled()) {
                            sb.append("Sullo schermo c'è scritto:\n'Accedere al terminale di sblocco':\n-T1\n-T2\n")
                              .append("\nDeve essere uno di quei vecchi sistemi.. scegli quello sbagliato e scatterà l'allarme.");
                            AdvObject.findInRoom(currentRoom, "T1").setVisible(true);
                            AdvObject.findInRoom(currentRoom, "T2").setVisible(true);
                        } else {
                            sb.append("Inutile, le telecamere sono già disattivate.");
                        }
                    } else {
                        sb.append("Non c'è nessun terminale qui.");
                    }
                }
        
                // 2.2 - Usare il terminale di sblocco 1 - ALLARME
                case "t1" -> {
                    if (currentRoom == game.getSalaSorveglianza()) {
                        if (AdvObject.findInRoom(game.getSalaSorveglianza(), "T1").isVisible()) {
                            sb.append("Diamine! Hai fatto scattare l'allarme!\n\nGAME OVER");
                            Concurrent.runAsync(() -> {
                                try {
                                    Thread.sleep(2000);
                                    EndObserver.finish(game, false);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } else {
                        sb.append("Non c'è nessun terminale qui.");
                    }
                }
        
                // 2.3 - Usare il terminale di sblocco 2 - Disabilita le telecamere
                case "t2" -> {
                    if (currentRoom == game.getSalaSorveglianza()) {
                        if (AdvObject.findInRoom(currentRoom, "T2").isVisible()) {
                            if (game.isSecurityEnabled()) {
                                game.setSecurity(false);
                                game.getCaveau().setLocked(false);
                                game.addScore(10);
                                sb.append("Terminale corretto per disattivare le telecamere. Ora puoi muoverti senza essere visto.");
                            } else {
                                sb.append("Le telecamere sono già disattivate.");
                            }
                        }
                    } else {
                        sb.append("Non c'è nessun terminale qui.");
                    }
                }
        
                // 3.0 - Usare il tastierino del caveau
                case "tastierino" -> {
                    if (currentRoom == game.getIngCaveau()) {
                        sb.append("Hai già disabilitato le misure di sicurezza, la porta del caveau è sbloccata.");
                    } else {
                        sb.append("Non c'è nessun tastierino qui.");
                    }
                }
        
                default -> sb.append("Temo che < ").append(name).append(" > non si possa usare qui.");
            }
        
            return sb.toString();
        }).get();
        
        System.out.println(PrettyPrint.print(result));
    }

    private String safeName(AdvObject obj) {
        return obj != null ? obj.getName().toLowerCase() : "";
    }
}