package com.mycompany.thediamondheist;

import com.mycompany.thediamondheist.type.Command;
import com.mycompany.thediamondheist.type.Player;
import com.mycompany.thediamondheist.type.Room;

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
