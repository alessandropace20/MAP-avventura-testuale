package com.mycompany.thediamondheist.impl;

import com.mycompany.thediamondheist.parser.ParserOutput;
import com.mycompany.thediamondheist.type.*;

/**
 * Observer che gestisce la visualizzazione dell’inventario.
 * - Elenca gli oggetti posseduti dal giocatore
 */


public class InventoryObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.INVENTORY) return;

        System.out.println(game.getPlayer().getInventory().toString());
    }
}
