/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import java.util.function.Supplier;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.PrettyPrint;

/**
 * Observer che gestisce i comandi di osservazione.
 * - Permette di esaminare stanze, oggetti o l’ambiente circostante
 */


public class LookObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (!(g instanceof GameDesc game)) return;
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.LOOK) return;
        String result = ((Supplier<String>) () -> {
            StringBuilder sb = new StringBuilder();
    
            Room cur = game.getPlayer().getCurrentRoom();
            sb.append("Ti trovi in: ").append(cur.getName()).append("\n\n");
            sb.append(cur.getDescription()).append("\n");
    
            if (!cur.getObjects().isEmpty()) {
                sb.append("\nVedi:\n");
                for (AdvObject o : cur.getObjects()) {
                    if (o.isVisible()) {
                    sb.append("- ").append(o.getName()).append("\n");
                    }
                }
            }
            return sb.toString();
        }).get();

        System.out.println(PrettyPrint.print(result));
    }
}
