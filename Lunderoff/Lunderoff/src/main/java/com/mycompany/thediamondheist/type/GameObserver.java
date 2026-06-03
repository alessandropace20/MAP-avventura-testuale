package com.mycompany.thediamondheist.type;
import com.mycompany.thediamondheist.parser.ParserOutput;

/**
 * Interfaccia Observer per il motore di gioco di The Diamond Heist.
 * - Definisce il metodo update(ParserOutput, GameDescription)
 * - Implementata dalle varie classi concrete (MoveObserver, PickUpObserver, ecc.)
 *   per reagire a comandi specifici del giocatore
 *
 * Utilizza il pattern Observer per separare la logica dei comandi
 * dalla gestione principale (GameController).
 */

public interface GameObserver {
    void update(ParserOutput p, Object game);
}
