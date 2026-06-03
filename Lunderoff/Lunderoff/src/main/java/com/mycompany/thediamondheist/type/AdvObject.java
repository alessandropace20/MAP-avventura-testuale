package com.mycompany.thediamondheist.type;

/**
 * Oggetto interattivo dell’avventura.
 * Può essere prendibile o solo osservabile.
 */
public class AdvObject {

    private final int id;
    private String name;
    private String description;
    private boolean visible;
    private boolean pickupable;

    public AdvObject(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.visible = true;
        this.pickupable = false;
    }

    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public boolean isPickupable() { return pickupable; }
    public void setPickupable(boolean pickupable) { this.pickupable = pickupable; }

    @Override
    public String toString() {
        return name + " (" + description + ")";
    }
}
