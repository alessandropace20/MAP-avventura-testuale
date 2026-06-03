/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package code.yankton_bank.type;

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

