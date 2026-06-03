package com.mycompany.thediamondheist.parser;

import com.mycompany.thediamondheist.type.Command;
import com.mycompany.thediamondheist.type.AdvObject;

/**
 * Risultato dell’analisi del Parser.
 * - Contiene il comando riconosciuto (verbo)
 * - Eventuali oggetti coinvolti (target, strumenti)
 */


public class ParserOutput {
    private final Command command;
    private final AdvObject object;

    public ParserOutput(Command command, AdvObject object) {
        this.command = command;
        this.object = object;
    }

    public Command getCommand() { return command; }
    public AdvObject getObject() { return object; }
}
