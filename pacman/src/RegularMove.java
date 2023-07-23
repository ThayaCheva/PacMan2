// Team 03
// Rohit Sandeep Ambakkat 1200197
// Thaya Chevaphatrakul 1167144
// Tran Than Han Ha 1472202
package src;

import ch.aplu.jgamegrid.GGKeyRepeatListener;
import ch.aplu.jgamegrid.Location;

import java.awt.event.KeyEvent;

public class RegularMove implements MovementStrategy, GGKeyRepeatListener {
    private PacActor pacActor;
    public RegularMove(PacActor pacActor) {
        this.pacActor = pacActor;
    }
    @Override
    public void keyRepeated(int keyCode)
    {
        if (pacActor.getIsAuto()) {
            return;
        }
        if (pacActor.isRemoved())  // Already removed
            return;
        Location next = null;
        switch (keyCode)
        {
            case KeyEvent.VK_LEFT:
                next = pacActor.getLocation().getNeighbourLocation(Location.WEST);
                pacActor.setDirection(Location.WEST);
                break;
            case KeyEvent.VK_UP:
                next = pacActor.getLocation().getNeighbourLocation(Location.NORTH);
                pacActor.setDirection(Location.NORTH);
                break;
            case KeyEvent.VK_RIGHT:
                next = pacActor.getLocation().getNeighbourLocation(Location.EAST);
                pacActor.setDirection(Location.EAST);
                break;
            case KeyEvent.VK_DOWN:
                next = pacActor.getLocation().getNeighbourLocation(Location.SOUTH);
                pacActor.setDirection(Location.SOUTH);
                break;
        }
        if (next != null && pacActor.canMove(next))
        {
            pacActor.setLocation(next);
            pacActor.eatPill(next);
            pacActor.getGame().getPortal().teleportActor("pacman");
        }
    }
}
