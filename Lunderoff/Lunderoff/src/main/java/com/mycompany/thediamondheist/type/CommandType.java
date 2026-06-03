package com.mycompany.thediamondheist.type;

/**
 * Enumerazione dei tipi di comando supportati in The Diamond Heist.
 * - Definisce le categorie principali di azioni che il giocatore può compiere
 *   (es. movimento, apertura, uso oggetti, raccolta, osservazione, inventario, aiuto, fine).
 *
 * Utilizzata dal Parser per classificare i comandi e dagli Observer
 * per determinare la logica da eseguire.
 */


public enum CommandType {
    NORTH, SOUTH, EAST, WEST,
    LOOK, PICK_UP, USE, OPEN, INVENTORY, HELP, END, SAVE, LOAD,
    UNKNOWN
}
