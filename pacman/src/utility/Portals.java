// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src.utility;

import ch.aplu.jgamegrid.Location;
import src.PortalsManager;

import java.util.ArrayList;

public class Portals {
    public PortalsManager portal;
    public Portals(ArrayList<ArrayList<Location>> allPortals) {
        this.portal = new PortalsManager(allPortals);
    }

    public void teleportActor(String type) {
        portal.checkLocation(type);
    }
}
