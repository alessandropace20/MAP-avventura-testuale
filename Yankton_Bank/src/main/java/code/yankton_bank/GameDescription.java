/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank;

import code.yankton_bank.type.Command;
import code.yankton_bank.type.Player;
import code.yankton_bank.type.Room;

import java.util.List;

/**
 * Interfaccia della descrizione di gioco.
 * - Definisce init(), testo introduttivo/finale, comandi e player
 */

public interface GameDescription {
    void init();
    String getIntro();
    String getEnding();
    List<Command> getCommands();
    Player getPlayer();

    default void onEnter(Room r) {}
}