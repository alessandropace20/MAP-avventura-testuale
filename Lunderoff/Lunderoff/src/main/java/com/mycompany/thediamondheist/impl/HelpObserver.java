package com.mycompany.thediamondheist.impl;

import com.mycompany.thediamondheist.parser.ParserOutput;
import com.mycompany.thediamondheist.type.*;

/**
 * Observer che mostra l’elenco dei comandi disponibili.
 */


public class HelpObserver implements GameObserver {

    @Override
    public void update(ParserOutput p, Object g) {
        if (p.getCommand() == null || p.getCommand().getType() != CommandType.HELP) return;

        System.out.println("Comandi disponibili:");
        System.out.println("- vai <nord/sud/est/ovest/su/giù>");
        System.out.println("- guarda");
        System.out.println("- prendi <oggetto>");
        System.out.println("- usa <oggetto>");
        System.out.println("- apri <oggetto>");
        System.out.println("- inventario");
        System.out.println("- aiuto");
        System.out.println("- fine");
    }
}
