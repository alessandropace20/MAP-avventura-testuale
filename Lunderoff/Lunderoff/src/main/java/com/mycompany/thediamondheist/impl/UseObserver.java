package com.mycompany.thediamondheist.impl;

import com.mycompany.thediamondheist.parser.ParserOutput;
import com.mycompany.thediamondheist.type.*;
import com.mycompany.thediamondheist.util.MusicHandler; // [AUDIO]


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
                
                if (currentRoom == game.getIngresso()) {                //sbloccare l'ingresso 
                System.out.println("======================================================");
                System.out.println("Usi le pinze per tagliare i fili della videocamera del retro.");
                System.out.println("\nAbbiamo pochi minuti prima che si accorga cosa sia successo realmente.");
                System.out.println("Per il momento, si godrà un piacevole fermo-immagine...");
                game.addScore(5);
                return;

                } 

                else if (currentRoom == game.getSalaSorveglianza()) {        //disattivare le telecamere
                    System.out.println("======================================================");
                    System.out.println("Usi le pinze per tagliare i fili della sala di sorveglianza");
                    System.out.println("\nNiente telecamere... niente allarme!");
                    System.out.println("Utile... se non passa nessuno a spararti...");
                    game.addScore(5);
                    return;

                }

                else {

                    System.out.println("Non ti conviene tagliare qualcosa qui con le pinze.");
                    return;
                }
            }
            else {

                System.out.println("Non hai delle pinze.");
                return;
            }
        } // Termine blocco oggetto pinze

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

        // 2.1 - Usare il terminale ()
        if (name.equals("terminale")) {
            if (currentRoom != game.getSalaSorveglianza()) {
                System.out.println("Non c'è nessun terminale qui.");
                return;
            }
            if (!game.isCamerasEnabled()) {
                game.setCameras(true);
                game.addScore(10);
                System.out.println("Usi il terminale per disattivare le telecamere. Ora puoi muoverti senza essere visto.");
            } else {
                System.out.println("Le telecamere sono già disattivate.");
            }
            return;
        }

        System.out.println("Non puoi usare " + name + " qui.");
    }

    private String safeName(AdvObject obj) {
        return obj != null ? obj.getName().toLowerCase() : "";
    }
}
