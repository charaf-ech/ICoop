package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public final class Spawn extends ICoopArea {

    private final DialogHandler dialogHandler;
    private boolean isFirstUpdate = true; // Pour ne l'afficher qu'une seule fois !

    public Spawn(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    // Player spawn positions for both players.
    @Override
    public ArrayList<DiscreteCoordinates> getPlayerSpawnPosition() {
        ArrayList<DiscreteCoordinates> coords = new ArrayList<>();
        coords.add(new DiscreteCoordinates(13,6));
        coords.add(new DiscreteCoordinates(14,7));
        return coords;
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        // Test Rock placed near spawn for explosion demo.
        Rock rock = new Rock(this, Orientation.UP, new DiscreteCoordinates(10, 10));
        registerActor(rock);

        // Test Explosive next to it; primes from player view interaction and destroys the rock.
        Explosive bomb = new Explosive(this, new DiscreteCoordinates(11, 10), 60);
        registerActor(bomb);

        // Door sending both players to OrbWay (arrival cells provided in list order).
        Door door = new Door(this, "OrbWay",
                List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5)),
                Orientation.DOWN,
                new DiscreteCoordinates(19, 15), // Position of the door in Spawn
                List.of(new DiscreteCoordinates(19, 16)), Logic.TRUE
        );
        registerActor(door);
        // --- TEST WALLS ---
        // Create a Fire Wall (Hurts the Blue Player)
        ElementalWall fireWall = new ElementalWall(this, Orientation.UP, new DiscreteCoordinates(10, 12), Element.FIRE);
        registerActor(fireWall);

        // Create a Water Wall (Hurts the Red Player)
        ElementalWall waterWall = new ElementalWall(this, Orientation.UP, new DiscreteCoordinates(12, 12), Element.WATER);
        registerActor(waterWall);

        Orb waterOrb = new Orb(this, Orientation.UP, new DiscreteCoordinates(12, 11), Element.WATER);
        registerActor(waterOrb);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Au tout premier "tick" du jeu, on lance le dialogue de bienvenue
        if (isFirstUpdate) {
            dialogHandler.publish(new Dialog("welcome"));
            isFirstUpdate = false;
        }
    }

    @Override
    public String getTitle() { return "Spawn";}
}