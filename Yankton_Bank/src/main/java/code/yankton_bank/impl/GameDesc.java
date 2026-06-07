/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.impl;

import code.yankton_bank.GameDescription;
import code.yankton_bank.type.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Descrizione e stato di gioco di Yankton Bank.
* - Inizializza stanze, oggetti, collegamenti
* - Mantiene lo stato del player, inventario, punteggio
* - Espone metodi per logiche particolari (es. disattivare telecamere, forzare serrature)
*/

public class GameDesc implements GameDescription {
    
    private Player player;
    private final List<Command> commands = new ArrayList<>();
    
    private Room ingresso, stanzinoPulizie, salaSorveglianza, corridoio, ingressoCaveau, caveau, uscita;

    private boolean lockerState = false;
    private boolean cameraState = true;
    private boolean securityState = true;
    
    private int dangerLVL = 0;
    private int score = 0;
    private final Set<Integer> visited = new HashSet<>();
    
    @Override
    public void onEnter(Room r) {
        if (r != null && visited.add(r.getId())) {
            addScore(5); // Incrementa il punteggio per ogni stanza visitata per la prima volta
        }
    }
    
    @Override
    public void init() {
        player = new Player("Michael De Santa");
        
        // Inizializzazione delle stanze
        ingresso = new Room(
            1, false, "Ingresso",
            "Ingresso posteriore dell'edificio, non si sa mai.");
        stanzinoPulizie = new Room(
            2, true, "Stanzino delle Pulizie",
            "Uno stanzino con attrezzature per le pulizie. Potrebbe contenere qualcosa di utile.");
        salaSorveglianza = new Room(
            3, true, "Sala Telecamere",
            "Una stanza piena di monitor e terminali. Devi trovare il terminale corretto per disattivare le telecamere.");
        corridoio = new Room(
            4,false, "Corridoio",
            "Un lungo corridoio con diverse porte che conducono a zone riservate.");
        ingressoCaveau = new Room(
            5,false, "Ingresso del Caveau",
            "Il caveau della banca. Se hai disattivato tutte le misure di sicurezza, potresti essere in grado di aprirlo.");
        caveau = new Room(
            6,true, "Caveau",
            "Il caveau della banca. Ci siamo quasi.");
        uscita = new Room(
            8,false, "Punto di fuga",
            "Il punto di fuga.");
        
        //Mappa
        ingresso.setNorth(stanzinoPulizie);

        stanzinoPulizie.setSouth(ingresso);
        stanzinoPulizie.setNorth(salaSorveglianza);
        
        salaSorveglianza.setSouth(stanzinoPulizie);
        salaSorveglianza.setEast(corridoio);
        
        corridoio.setWest(salaSorveglianza);
        corridoio.setNorth(ingressoCaveau);
        
        ingressoCaveau.setSouth(corridoio);
        ingressoCaveau.setNorth(caveau);

        caveau.setSouth(ingressoCaveau);

        // Oggetti
        AdvObject pinze = new AdvObject(101, "Pinze", "Pinze utili per tagliare fili.");

        AdvObject grimaldello = new AdvObject(102, "Grimaldello", "Utile per forzare porte.");
        
        AdvObject porta = new AdvObject(0, "Porta", "");
        ingresso.addObject(porta);
        stanzinoPulizie.addObject(porta);
        ingressoCaveau.addObject(porta);
        porta.setPickupable(false);
        porta.setVisible(true);
        
        AdvObject telecamera = new AdvObject(1, "Telecamera di sorveglianza.",
            "\nConverebbe tagliare il cavo di collegamento prima di provare ad entrare.");
        ingresso.addObject(telecamera);
        telecamera.setPickupable(false);
        telecamera.setVisible(true);

        AdvObject armadietto = new AdvObject(103, "Armadietto", "Un armadietto chiuso a chiave.");
        stanzinoPulizie.addObject(armadietto);
        armadietto.setPickupable(false);

        AdvObject chiavi = new AdvObject(104, "Chiavi", "Un mazzo consistente di chiavi.");
        stanzinoPulizie.addObject(chiavi);
        chiavi.setPickupable(true);
        chiavi.setVisible(false);

        AdvObject terminale = new AdvObject(105, "Terminale", "Un terminale per il controllo delle telecamere.");
        salaSorveglianza.addObject(terminale);
        terminale.setPickupable(false);
        terminale.setVisible(true);

        AdvObject t1 = new AdvObject(106, "T1", "Un terminale per il controllo delle telecamere.");
        salaSorveglianza.addObject(t1);
        t1.setPickupable(false);
        t1.setVisible(false);

        AdvObject t2 = new AdvObject(107, "T2", "Un terminale per il controllo delle telecamere.");
        salaSorveglianza.addObject(t2);
        t2.setPickupable(false);
        t2.setVisible(false);

        AdvObject tastierino = new AdvObject(108, "Tastierino", "Un tastierino... non hai il codice della porta blindata e dubito lo otterrai.");
        ingressoCaveau.addObject(tastierino);
        tastierino.setPickupable(false);

        AdvObject cassetta = new AdvObject(109, "Cassetta", "La cassetta di sicurezza... dovremmo esserci.");
        caveau.addObject(cassetta);
        cassetta.setPickupable(false);
        cassetta.setVisible(true);

        AdvObject oro = new AdvObject(110, "Oro", "[OBIETTIVO]\nLa riserva d'oro del trafficante. Prendilo e scappa!");
        caveau.addObject(oro);
        oro.setPickupable(true);
        oro.setVisible(false);

        // Comandi
        commands.clear();
        commands.add(new Command("nord", CommandType.NORTH));
        commands.add(new Command("sud", CommandType.SOUTH));
        commands.add(new Command("est", CommandType.EAST));
        commands.add(new Command("ovest", CommandType.WEST));
        commands.add(new Command("guarda", CommandType.LOOK));
        commands.add(new Command("prendi", CommandType.PICK_UP));
        commands.add(new Command("usa", CommandType.USE));
        commands.add(new Command("apri", CommandType.OPEN));
        commands.add(new Command("inventario", CommandType.INVENTORY));
        commands.add(new Command("aiuto", CommandType.HELP));
        commands.add(new Command("fine", CommandType.END));

        player.setCurrentRoom(ingresso);
        player.getInventory().add(pinze);
        player.getInventory().add(grimaldello);
    }
    
    public Room getIngresso() { return ingresso; }
    public Room getStanzinoPulizie() { return stanzinoPulizie; }
    public Room getSalaSorveglianza() { return salaSorveglianza; }
    public Room getCorridoio() { return corridoio; }
    public Room getIngCaveau() { return ingressoCaveau; }
    public Room getCaveau() { return caveau; }
    
    // Metodo per incrementare il livello di pericolo
    public void incrementDanger(int level) {
        dangerLVL += level;
        if (dangerLVL >= 10) {
            getEnding();
        }
    }
    
    // Metodo per punteggio
    public void addScore(int points) { score += points; }
    public void decreaseScore(int points) { score -= points; }
    public int getScore() { return score; }
    
    // Boolean methods
    public boolean isLockerOpen() { return lockerState; }
    public boolean isCameraEnabled() { return cameraState; }
    public boolean isSecurityEnabled() { return securityState; }

    // Setter
    public void setLocker(boolean b) { this.lockerState = b; }
    public void setCamera(boolean b) { this.cameraState = b; }
    public void setSecurity(boolean b) { this.securityState = b; }

    // Getter
    
    @Override public String getIntro() 
        { return "============ Città di Yankton, Texas, 2:30 AM ==============\n\n" +
            "Hai scoperto che un noto trafficante nasconde la sua riserva d'oro in una banca abbandonata.\n" +
            "Speriamo lo sia per davvero, siccome ora lo dovrai rubare.\n" +
            "Sarà pure trascurata, ma l'impianto e il sistema di sicurezza sembra essere tutt'ora attivo.\n" + 
            "Cerca di essere il più discreto possibile.\n\n"; }
    @Override public String getEnding() { return "La polizia ti ha catturato! Hai perso."; }
    @Override public List<Command> getCommands() { return commands; }
    @Override public Player getPlayer() { return player; }
}