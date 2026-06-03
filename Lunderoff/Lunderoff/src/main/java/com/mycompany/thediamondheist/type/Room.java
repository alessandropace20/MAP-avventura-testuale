package com.mycompany.thediamondheist.type;

import java.util.*;

/**
 * Modello di una stanza del gioco.
 * - Contiene nome, descrizione, immagine, uscite e oggetti presenti
 */


public class Room {

    private final int id;
    private boolean locked;
    private String name;
    private String description;

    private final Map<String, Room> exits = new HashMap<>();
    private final List<AdvObject> objects = new ArrayList<>();

    private String imagePath;

    public Room(int id, boolean locked, String name, String description) {
        this.id = id;
        this.locked = locked;
        this.name = name;
        this.description = description;
    }

    public int getId() { return id; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public void setExit(String direction, Room destination) {
        if (direction == null || destination == null) return;
        exits.put(direction.toLowerCase(Locale.ITALIAN), destination);
    }
    public Room getExit(String direction) {
        if (direction == null) return null;
        return exits.get(direction.toLowerCase(Locale.ITALIAN));
    }
    public Map<String, Room> getExits() {
        return Collections.unmodifiableMap(exits);
    }

    public List<AdvObject> getObjects() { return objects; }
    public void addObject(AdvObject obj) { if (obj != null) objects.add(obj); }
    public AdvObject removeObjectByName(String name) {
        if (name == null || name.isBlank()) return null;
        for (int i = 0; i < objects.size(); i++) {
            AdvObject o = objects.get(i);
            if (o != null && o.getName() != null && o.getName().equalsIgnoreCase(name)) {
                objects.remove(i);
                return o;
            }
        }
        return null;
    }

    public void setNorth(Room r) 
        { setExit("nord", r); }
    public void setSouth(Room r) 
        { setExit("sud", r); }
    public void setEast(Room r)  
        { setExit("est", r); }
    public void setWest(Room r)  
        { setExit("ovest", r); }
}
