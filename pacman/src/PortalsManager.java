// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;

public class PortalsManager {
    private ArrayList<Actor> subscribers = new ArrayList<>();
    private ArrayList<ArrayList<Location>> allPortals;
    public PortalsManager(ArrayList<ArrayList<Location>> allPortals) {
        this.allPortals = allPortals;
    }

    public void subscribe(Actor actor) {
        subscribers.add(actor);
    }

    public void unsubscribe(Actor actor) {
        subscribers.remove(actor);
    }

    // Checks if either pacman or a monster is in a portal
    public void checkLocation(String type) {
        for (Actor sub : subscribers) {
            if (sub instanceof PacActor && type.equals("pacman")) {
                PacActor pacActor = (PacActor) sub;
                for (ArrayList<Location> p : allPortals) {
                    if (p.size() % 2 == 0 && p.size() > 0 && sub.getLocation().equals(p.get(0))) {
                        pacActor.teleport(p.get(1));
                    }
                    else if (p.size() % 2 == 0 && p.size() > 0 && sub.getLocation().equals(p.get(1))) {
                        pacActor.teleport(p.get(0));
                    }
                }
            }
            else if (sub instanceof Monster) {
                Monster monster = (Monster) sub;
                for (ArrayList<Location> p : allPortals) {
                    if (p.size() % 2 == 0 && p.size() > 0 && sub.getLocation().equals(p.get(0))) {
                        monster.teleport(p.get(1));
                    }
                    else if (p.size() % 2 == 0 && p.size() > 0 && sub.getLocation().equals(p.get(1))) {
                        monster.teleport(p.get(0));
                    }
                }
            }
        }
    }
}
