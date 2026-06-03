package com.mycompany.thediamondheist.impl;

import com.mycompany.thediamondheist.GameDescription;
import com.mycompany.thediamondheist.type.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* Descrizione e stato di gioco di Lunderoff.
* - Inizializza stanze, oggetti, collegamenti
* - Mantiene lo stato del player, inventario, punteggio
* - Espone metodi per logiche particolari (es. disattivare telecamere, forzare serrature)
*/

public class GameDesc implements GameDescription {
    
    private Player player;
    private final List<Command> commands = new ArrayList<>();
    
    private Room ingresso, stanzinoPulizie, salaSorveglianza, corridoio, zonaSensori, caveau, uscita;

    private boolean lockerState = false;
    private boolean cameraState = true;
    private boolean camerasState = true;
    private boolean sensorState = false;
    private boolean caveauState = false;
    
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
            "Sei davanti alla porta d'ingresso dell'edificio. Devi trovare un modo per entrare.");
        stanzinoPulizie = new Room(
            2, true, "Stanzino delle Pulizie",
            "Uno stanzino con attrezzature per le pulizie. Potrebbe contenere qualcosa di utile.");
        salaSorveglianza = new Room(
            3, true, "Sala Telecamere",
            "Una stanza piena di monitor e terminali. Devi trovare il terminale corretto per disattivare le telecamere.");
        corridoio = new Room(
            4,false, "Corridoio",
            "Un lungo corridoio con diverse porte che conducono a zone riservate.");
        zonaSensori = new Room(
            5,false, "Zona Sensori",
            "Un'area con sensori a pressione sul pavimento. Devi attraversarla senza far scattare l'allarme.");
        caveau = new Room(
            6,false, "Caveau",
            "Il caveau della banca. Devi forzare la serratura per accedere al bottino.");
        uscita = new Room(
            7,false, "Uscita di Emergenza",
            "Il punto di fuga. Devi tornare qui con il bottino prima che il livello di pericolo raggiunga il massimo.");
        
        //Mappa
        ingresso.setNorth(stanzinoPulizie);

        stanzinoPulizie.setSouth(ingresso);
        stanzinoPulizie.setNorth(salaSorveglianza);
        
        salaSorveglianza.setSouth(stanzinoPulizie);
        salaSorveglianza.setEast(corridoio);
        
        corridoio.setSouth(salaSorveglianza);
        corridoio.setWest(zonaSensori);
        
        zonaSensori.setEast(corridoio);
        zonaSensori.setNorth(caveau);
        
        caveau.setSouth(zonaSensori);

        // Oggetti
        AdvObject pinze = new AdvObject(101, "Pinze", "Pinze utili per tagliare fili.");
        ingresso.addObject(pinze);
        pinze.setPickupable(true);

        AdvObject grimaldello = new AdvObject(101, "Grimaldello", "Utile per forzare porte.");
        ingresso.addObject(grimaldello);
        grimaldello.setPickupable(true);

        AdvObject armadietto = new AdvObject(103, "Armadietto", "Un armadietto chiuso a chiave.");
        stanzinoPulizie.addObject(armadietto);
        armadietto.setPickupable(false);

        AdvObject chiavi = new AdvObject(112, "Chiavi", "Un mazzo consistente di chiavi.");
        stanzinoPulizie.addObject(chiavi);
        chiavi.setPickupable(true);
        chiavi.setVisible(false);

        AdvObject terminale = new AdvObject(104, "Terminale", "Un terminale per il controllo delle telecamere.");
        salaSorveglianza.addObject(terminale);
        terminale.setPickupable(false);

        AdvObject istruzioni = new AdvObject(107, "Istruzioni", "Un foglio strano... c'è scritto: 'come 4, 8 e 18...'");
        salaSorveglianza.addObject(istruzioni);
        istruzioni.setPickupable(false);

        AdvObject tastierino = new AdvObject(108, "Tastierino", "Un tastierino per inserire un codice.");
        zonaSensori.addObject(tastierino);
        tastierino.setPickupable(false);

        AdvObject sensore1 = new AdvObject(109, "Sensore1", "Una serie di sensori a pressione sul pavimento.");
        zonaSensori.addObject(sensore1);
        sensore1.setPickupable(false);

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
    }
    
    public Room getIngresso() { return ingresso; }
    public Room getStanzinoPulizie() { return stanzinoPulizie; }
    public Room getSalaSorveglianza() { return salaSorveglianza; }
    public Room getCorridoioPrincipale() { return corridoio; }
    public Room getZonaSensori() { return zonaSensori; }
    public Room getCaveau() { return caveau; }
    public Room getUscita() { return uscita; }
    
    // Metodo per incrementare il livello di pericolo
    public void incrementDanger(int level) {
        dangerLVL += level;
        if (dangerLVL >= 10) {
            getEnding();
        }
    }
    
    // Metodo per punteggio
    public void addScore(int points) { score += points; }
    public void decreaseScore(int points) { score += points; }
    public int getDangerLVL() { return dangerLVL; }
    public int getScore() { return score; }
    
    // Boolean methods
    public boolean isLockerOpen() { return lockerState; }
    public boolean isCameraEnabled() { return cameraState; }
    public boolean isCamerasEnabled() { return camerasState; }
    public boolean isSensorsDisabled() { return sensorState; }
    public boolean isCaveauOpen() { return caveauState; }

    // Setter
    public void setLocker(boolean b) { this.lockerState = b; }
    public void setCamera(boolean b) { this.cameraState = b; }
    public void setCameras(boolean b) { this.camerasState = b; }
    public void setSensors(boolean b) { this.sensorState = b; }
    public void setCaveauOpen(boolean b) { this.caveauState = b; }

    // Getter
    
    @Override public String getIntro() { return "Benvenuto a Lunderoff..."; }
    @Override public String getEnding() { return "La polizia ti ha catturato! Hai perso."; }
    @Override public List<Command> getCommands() { return commands; }
    @Override public Player getPlayer() { return player; }
}

