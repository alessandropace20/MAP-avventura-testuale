/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.type;

/**
 * Enumerazione dei tipi di comando supportati in Yankton Bank.
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