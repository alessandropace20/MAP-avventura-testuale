/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.parser;

import code.yankton_bank.type.Command;
import code.yankton_bank.type.AdvObject;

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

