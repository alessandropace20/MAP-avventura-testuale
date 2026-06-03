package com.mycompany.thediamondheist.type;

/**
 * Modello di un comando del gioco The Diamond Heist.
 * - Contiene le informazioni necessarie per il riconoscimento da parte del Parser
 *   (nome, alias, tipo di comando)
 * - Associa un CommandType che identifica la logica collegata
 *
 * Utilizzato dal Parser per confrontare l'input del giocatore con i comandi disponibili
 * e produrre un ParserOutput da passare agli Observer.
 */


public class Command {
    private final String word;
    private final CommandType type;

    public Command(String word, CommandType type) {
        this.word = word;
        this.type = type;
    }

    public String getWord() { return word; }
    public CommandType getType() { return type; }
}
