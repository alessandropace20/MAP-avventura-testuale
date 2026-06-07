/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.PrettyPrint;

/**
 * Observer che gestisce la visualizzazione dell’inventario.
 * - Elenca gli oggetti posseduti dal giocatore
 */


public class InventoryObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.INVENTORY) return;

        System.out.println(PrettyPrint.print(game.getPlayer().getInventory().toString()));
    }
}
