package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public final class OrbWay extends ICoopArea {

    // Player spawn positions when entering OrbWay from Spawn.
    @Override
    public ArrayList<DiscreteCoordinates> getPlayerSpawnPosition() {
        ArrayList<DiscreteCoordinates> coords = new ArrayList<>();
        coords.add(new DiscreteCoordinates(1,12));
        coords.add(new DiscreteCoordinates(1,5));
        return coords;
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        // Door 1: top-left gateway back to Spawn (covers y=10..14 on x=0).
        Door door1 = new Door(this, "Spawn",
                List.of(new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)),
                Orientation.LEFT,
                new DiscreteCoordinates(0, 14), // Main cell
                List.of(  // Extra cells (all in OrbWay coordinates!)
                        new DiscreteCoordinates(0, 13),
                        new DiscreteCoordinates(0, 12),
                        new DiscreteCoordinates(0, 11),
                        new DiscreteCoordinates(0, 10)
                ),
                Logic.TRUE
        );

        // Door 2: bottom-left gateway back to Spawn (covers y=4..8 on x=0).
        Door door2 = new Door(
                this,
                "Spawn",
                List.of(new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)),
                Orientation.LEFT,
                new DiscreteCoordinates(0, 8), // Main cell
                List.of(  // Extra cells
                        new DiscreteCoordinates(0, 7),
                        new DiscreteCoordinates(0, 6),
                        new DiscreteCoordinates(0, 5),
                        new DiscreteCoordinates(0, 4)
                ),
                Logic.TRUE
        );

        registerActor(door1);
        registerActor(door2);
    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }
}