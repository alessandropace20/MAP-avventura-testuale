package com.mycompany.thediamondheist.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Contenitore per gli oggetti presenti in una stanza.
 */


public class AdvObjectContainer extends AdvObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<AdvObject> objects = new ArrayList<>();

    public AdvObjectContainer(int id, String name, String description) {
        super(id, name, description);
    }

    public List<AdvObject> getObjects() { return objects; }
    public void addObject(AdvObject obj) { objects.add(obj); }
}
