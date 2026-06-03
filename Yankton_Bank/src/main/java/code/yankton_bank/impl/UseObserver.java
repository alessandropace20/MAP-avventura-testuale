/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.MusicHandler; // [AUDIO]


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

                    System.out.println("======================================================");
                    System.out.println("Usi le pinze per tagliare i cavi elettrici esterni.\nTelecamera disabilitata.");
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

                    System.out.println("======================================================");
                    System.out.println("Usi il grimaldello per forzare la porta.\nPorta nord sbloccata");
                    game.getStanzinoPulizie().setLocked(false);

                    if(game.isCameraEnabled()){
                        game.decreaseScore(10);
                    }
                    else {
                        game.addScore(10);
                    }
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
                System.out.println("======================================================");
                if(game.getSalaSorveglianza().isLocked()){
                    game.getSalaSorveglianza().setLocked(false);
                    System.out.println("\nPorta nord sbloccata.");
                }
                else{
                    System.out.println("\nPorta già sbloccata.");
                }
                return;
                }
            }

            else {
                System.out.println("Non hai delle chiavi.");
                return;
            }
        } // Termine blocco oggetto chiavi

        // 2.1 - Usare il terminale principale ()
        if (name.equals("terminale")) {
            if (currentRoom != game.getSalaSorveglianza()) {
                System.out.println("Non c'è nessun terminale qui.");
                return;
            }
            if (game.isCamerasEnabled()) {
                System.out.println("======================================================");
                System.out.println("Sullo schermo c'è scritto:\n'Accedere al terminale di sblocco':\n-T1\n-T2\n");
                System.out.println("Deve essere uno di quei vecchi sistemi.. scegli quello sbagliato e sei fregato.");
                
                AdvObject.findInRoom(currentRoom, "T1").setVisible(true);
                AdvObject.findInRoom(currentRoom, "T2").setVisible(true);
            } else {
                System.out.println("Inutile, le telecamere sono già disattivate.");
            }
            return;
        }

        // 2.2 - Usare il terminale di sblocco 1 () ALLARME !!!!!
        if (name.equals("t1")) {
            if (currentRoom != game.getSalaSorveglianza()) {
                System.out.println("Non c'è nessun terminale qui.");
                return;
            }
            if(AdvObject.findInRoom(game.getSalaSorveglianza(), "T1").isVisible()){
                EndObserver.finish(game, false);
                return;
            }
        }

        // 2.3 - Usare il terminale di sblocco 2 () CORRETTO, Disabilita le telecamere
        if (name.equals("t2")) {
            if (currentRoom != game.getSalaSorveglianza()) {
                System.out.println("Non c'è nessun terminale qui.");
                return;
            }
            if(AdvObject.findInRoom(game.getSalaSorveglianza(), "T2").isVisible()){
                if (game.isCamerasEnabled()) {
                    game.setCameras(false);
                    game.addScore(10);
                    System.out.println("======================================================");
                    System.out.println("Terminale corretto per disattivare le telecamere. Ora puoi muoverti senza essere visto.");
                } else {
                    System.out.println("Le telecamere sono già disattivate.");
                }
                return;
            }
        }

        System.out.println("Non puoi usare " + name + " qui.");
    }

    private String safeName(AdvObject obj) {
        return obj != null ? obj.getName().toLowerCase() : "";
    }
}