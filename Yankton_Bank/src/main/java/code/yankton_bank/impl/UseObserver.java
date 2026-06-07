/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.Concurrent;

/**
 * Observer che gestisce i comandi di utilizzo.
 * - Permette di usare oggetti dell’inventario o presenti nella stanza
 * - Può attivare eventi o cambiare lo stato del gioco
 */
public class UseObserver implements GameObserver {

    @Override
    public void update(ParserOutput parser, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (parser.getCommand() == null || parser.getCommand().getType() != CommandType.USE) return;

        AdvObject currentObj = parser.getObject();
        Player currentPlayer = game.getPlayer();
        Room currentRoom = currentPlayer.getCurrentRoom();

        if (currentObj == null) {
            System.out.println("Usare cosa?");
            return;
        }

        String name = safeName(currentObj);

        // 1.0 - Usare le pinze
        if (name.equals("pinze")) {
            if (currentPlayer.getInventory().containsByName("Pinze")) {
                if (currentRoom == game.getIngresso()) {

                    System.out.println("\n======================================================\n");
                    System.out.println("Usi le pinze per tagliare i cavi elettrici esterni.\nTelecamera disabilitata.");
                    System.out.println("\n======================================================\n");
                    game.addScore(15);
                    game.setCamera(false);
                    return;
                }
            }
            else {

                System.out.println("Non hai delle pinze.");
                return;
            }
        }

        // 1.1 - Usare il grimaldello
        if (name.equals("grimaldello")){
            if (currentPlayer.getInventory().containsByName("Grimaldello")) {
                if (currentRoom == game.getIngresso()) {
                    System.out.println("\n======================================================\n");
                    System.out.println("Usi il grimaldello per forzare la porta.\nPorta nord sbloccata.");
                    System.out.println("\n======================================================\n");
                    game.getStanzinoPulizie().setLocked(false);

                    if(game.isCameraEnabled()){
                        game.decreaseScore(10);
                    }
                    else {
                        game.addScore(15);
                    }
                    return;
                }
                if (currentRoom == game.getIngCaveau()) {
                    System.out.println("\n======================================================\n");
                    System.out.println("Sul serio vuoi aprire una porta blindata da 2 quintali con un grimaldello?\n" +
                    "Devi trovare un altro modo per aprirla.\nTi converrebbe disattivare i sistemi di sicurezza, anche se non mi sembri un tipo sveglio.");
                    System.out.println("\n======================================================\n");
                    return;
                }
            }
            else {
                System.out.println("Non hai alcun grimaldello.");
                return;
            }
        }

        // 2.0 - Usare le chiavi
        if (name.equals("chiavi")) {
            if (currentPlayer.getInventory().containsByName("Chiavi")) {
                if(currentRoom == game.getStanzinoPulizie()) {
                    System.out.println("\n======================================================\n");
                    if(game.getSalaSorveglianza().isLocked()){
                        System.out.println("Porta nord sbloccata.");
                        game.getSalaSorveglianza().setLocked(false);
                        game.addScore(15);
                    }
                    else {
                        System.out.println("Porta già sbloccata.");
                    }
                    System.out.println("\n======================================================\n");
                    return;
                }
                if(currentRoom == game.getIngCaveau()) {
                    System.out.println("\n======================================================\n");
                    System.out.println("Non credo che in un mazzo di chiavi trovato in uno stanzino delle pulizie, ci sia una chiave che possa aprire" +
                        " una porta blindata spessa più del tuo cervello, genio...");
                    System.out.println("\n======================================================\n");
                    return;
                }
            }
            else {
                System.out.println("Non hai delle chiavi.");
                return;
            }
        }

        // 2.1 - Usare il terminale principale ()
        if (name.equals("terminale")) {
            if (currentRoom == game.getSalaSorveglianza()) {
                System.out.println("\n======================================================\n");
                if (game.isSecurityEnabled()) {
                    System.out.println("Sullo schermo c'è scritto:\n'Accedere al terminale di sblocco':\n-T1\n-T2\n");
                    System.out.println("Deve essere uno di quei vecchi sistemi.. scegli quello sbagliato e scatterà l'allarme.");                
                    AdvObject.findInRoom(currentRoom, "T1").setVisible(true);
                    AdvObject.findInRoom(currentRoom, "T2").setVisible(true);
                } 
                else {
                    System.out.println("Inutile, le telecamere sono già disattivate.");
                }
                System.out.println("\n======================================================\n");
                return;
            }
            else {
                System.out.println("Non c'è nessun terminale qui.");
                return;
            }
            
        }

        // 2.2 - Usare il terminale di sblocco 1 () ALLARME !!!!!
        if (name.equals("t1")) {
            if (currentRoom == game.getSalaSorveglianza()) {
                if(AdvObject.findInRoom(game.getSalaSorveglianza(), "T1").isVisible()){
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
                    return;
                }
            }
            else {
                System.out.println("Non c'è nessun terminale qui.");
                return;
            }
            
        }

        // 2.3 - Usare il terminale di sblocco 2 () CORRETTO, Disabilita le telecamere
        if (name.equals("t2")) {
            if (currentRoom == game.getSalaSorveglianza()) {
                if(AdvObject.findInRoom(currentRoom, "T2").isVisible()) {
                    System.out.println("\n======================================================\n");
                    if (game.isSecurityEnabled()) {
                        game.setSecurity(false);
                        game.getCaveau().setLocked(false);
                        game.addScore(10);
                        System.out.println("Terminale corretto per disattivare le telecamere. Ora puoi muoverti senza essere visto.");
                    }
                    else {
                        System.out.println("Le telecamere sono già disattivate.");
                    }
                    System.out.println("\n======================================================\n");
                    return;
                }
            }
            else {
                System.out.println("Non c'è nessun terminale qui.");
                return;
            }
            
        }

        // 3.0 - Usare il tastierino del caveau
        if (name.equals("tastierino")) {
            if (currentRoom == game.getIngCaveau()) {
                System.out.println("\n======================================================\n");
                System.out.println("Hai già disabilitato le misure di sicurezza, la porta del caveau è sbloccata.");
                System.out.println("\n======================================================\n");
                return;
            }
            else {
                System.out.println("Non c'è nessun tastierino qui.");
                return;
            }
        }

        System.out.println("Temo che < " + name + " > non si possa usare qui.");
    }

    private String safeName(AdvObject obj) {
        return obj != null ? obj.getName().toLowerCase() : "";
    }
}