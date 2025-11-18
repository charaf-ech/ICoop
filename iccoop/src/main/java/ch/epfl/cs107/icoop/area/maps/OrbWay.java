package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public final class OrbWay extends ICoopArea {

    @Override
    public ArrayList<DiscreteCoordinates> getPlayerSpawnPosition() {
        ArrayList<DiscreteCoordinates>coords=new ArrayList<>();
        coords.add(new DiscreteCoordinates(1,12));
        coords.add(new DiscreteCoordinates(1,5));
        return coords;
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        // Coordonnées d'arrivée dans Spawn (Rouge, Bleu)
        DiscreteCoordinates[] arrivalsInSpawn = new DiscreteCoordinates[]{
                new DiscreteCoordinates(18, 16),
                new DiscreteCoordinates(18, 15)
        };

        // Porte 1 (porte du haut)
        registerActor(new Door("Spawn", Logic.TRUE, this,
                new DiscreteCoordinates(0, 14), // Case principale
                // Cases additionnelles
                List.of(new DiscreteCoordinates(0,13), new DiscreteCoordinates(0,12), new DiscreteCoordinates(0,11), new DiscreteCoordinates(0,10)),
                arrivalsInSpawn // Coords d'arrivée
        ));

        // Porte 2 (porte du bas)
        registerActor(new Door("Spawn", Logic.TRUE, this,
                new DiscreteCoordinates(0, 8), // Case principale
                // Cases additionnelles
                List.of(new DiscreteCoordinates(0,7), new DiscreteCoordinates(0,6), new DiscreteCoordinates(0,5), new DiscreteCoordinates(0,4)),
                arrivalsInSpawn // Coords d'arrivée
        ));
    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }
}