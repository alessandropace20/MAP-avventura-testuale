package com.mycompany.thediamondheist.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventario del giocatore, contiene gli oggetti raccolti.
 */
public class Inventory {

    private final List<AdvObject> objects = new ArrayList<>();

    public List<AdvObject> getObjects() {
        return objects;
    }

    public void add(AdvObject obj) {
        if (obj != null) {
            objects.add(obj);
        }
    }

    public void remove(AdvObject obj) {
        if (obj != null) {
            objects.remove(obj);
        }
    }

    public AdvObject removeByName(String name) {
        if (name == null) return null;
        for (AdvObject o : objects) {
            if (o.getName().equalsIgnoreCase(name)) {
                objects.remove(o);
                return o;
            }
        }
        return null;
    }

    public AdvObject getByName(String name) {
        if (name == null) return null;
        for (AdvObject o : objects) {
            if (o.getName().equalsIgnoreCase(name)) {
                return o;
            }
        }
        return null;
    }

    public boolean containsByName(String name) {
        if (name == null) return false;
        String n = name.trim().toLowerCase();
        for (AdvObject o : objects) {
            if (o != null && o.getName() != null && o.getName().equalsIgnoreCase(n)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return objects.size();
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    @Override
    public String toString() {
        if (objects.isEmpty()) {
            return "Inventario vuoto.";
        }
        StringBuilder sb = new StringBuilder("Inventario:\n");
        for (AdvObject o : objects) {
            sb.append("- ").append(o.getName())
              .append(": ").append(o.getDescription())
              .append("\n");
        }
        return sb.toString();
    }
}
