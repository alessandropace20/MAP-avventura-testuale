/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.type;

/**
 * Modello del giocatore.
 * - Mantiene lo stato corrente: stanza attuale, inventario, punteggio
 */


public class Player {
    private String name;
    private Room currentRoom;
    private final Inventory inventory = new Inventory();

    public Player() { this("Player"); }
    public Player(String name) { this.name = name; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Room getCurrentRoom() { return currentRoom; }
    public void setCurrentRoom(Room currentRoom) { this.currentRoom = currentRoom; }

    public Inventory getInventory() { return inventory; }
}
