/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.parser.ParserOutput;
import code.yankton_bank.type.*;
import code.yankton_bank.util.PrettyPrint;

/**
 * Observer che mostra l’elenco dei comandi disponibili.
 */


public class HelpObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.HELP) return;

        System.out.println(PrettyPrint.print("""
            Comandi disponibili:
            - vai <nord/sud/est/ovest>
            - guarda
            - prendi <oggetto>
            - usa <oggetto>
            - apri <oggetto>
            - inventario
            - aiuto
            - fine
            """));
    }
}

