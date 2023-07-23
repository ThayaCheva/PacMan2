package src;

import ch.aplu.jgamegrid.Location;

public class AutoMove implements MovementStrategy {
    private PacActor pacActor;
    public AutoMove(PacActor pacActor) {
        this.pacActor = pacActor;
    }
    @Override
    public void keyRepeated(int keyCode) {
        moveInAutoMode();
    }

    private void moveInAutoMode() {
        if (pacActor.getPropertyMoves().size() > pacActor.getPropertyMoveIndex()) {
            pacActor.followPropertyMoves();
            return;
        }
        Location closestPill = pacActor.closestPillLocation();
        double oldDirection = pacActor.getDirection();

        Location.CompassDirection compassDir =
                pacActor.getLocation().get4CompassDirectionTo(closestPill);
        Location next = pacActor.getLocation().getNeighbourLocation(compassDir);
        pacActor.setDirection(compassDir);
        if (!pacActor.isVisited(next) && pacActor.canMove(next)) {
            pacActor.setLocation(next);
        } else {
            // normal movement
            int sign = pacActor.getRandomiser().nextDouble() < 0.5 ? 1 : -1;
            pacActor.setDirection(oldDirection);
            pacActor.turn(sign * 90);  // Try to turn left/right
            next = pacActor.getNextMoveLocation();
            if (pacActor.canMove(next)) {
                pacActor.setLocation(next);
            } else {
                pacActor.setDirection(oldDirection);
                next = pacActor.getNextMoveLocation();
                if (pacActor.canMove(next)) // Try to move forward
                {
                    pacActor.setLocation(next);
                } else {
                    pacActor.setDirection(oldDirection);
                    pacActor.turn(-sign * 90);  // Try to turn right/left
                    next = pacActor.getNextMoveLocation();
                    if (pacActor.canMove(next)) {
                        pacActor.setLocation(next);
                    } else {
                        pacActor.setDirection(oldDirection);
                        pacActor.turn(180);  // Turn backward
                        next = pacActor.getNextMoveLocation();
                        pacActor.setLocation(next);
                    }
                }
            }
        }
        pacActor.eatPill(next);
        pacActor.addVisitedList(next);
    }
}