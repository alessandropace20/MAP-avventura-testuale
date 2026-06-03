/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.type;

import code.yankton_bank.parser.ParserOutput;
/**
 * Interfaccia Observer per il motore di gioco di Yankton Bank.
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
