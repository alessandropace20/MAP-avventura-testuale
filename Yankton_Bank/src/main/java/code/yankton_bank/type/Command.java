/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.type;

/**
 * Modello di un comando del gioco Yankton Bank.
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