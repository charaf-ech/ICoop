package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;

public final class Spawn extends ICoopArea {
    @Override
    public ArrayList<DiscreteCoordinates> getPlayerSpawnPosition() {
        ArrayList<DiscreteCoordinates>coords=new ArrayList<>();
        coords.add(new DiscreteCoordinates(13,6));
        coords.add(new DiscreteCoordinates(14,7));
        return coords;
    }
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        // AJOUTEZ CECI (Tâche 2.4.1)
        // Coordonnées d'arrivée dans OrbWay (Rouge, Bleu)
        DiscreteCoordinates[] arrivals = new DiscreteCoordinates[]{
                new DiscreteCoordinates(1, 12),
                new DiscreteCoordinates(1, 5)
        };

        // Créez la porte vers OrbWay
        Door toOrbWay = new Door("OrbWay", Logic.TRUE, this,
                new DiscreteCoordinates(19, 15), // Case principale
                arrivals, // Coords d'arrivée
                new DiscreteCoordinates(19, 16)  // Autre case occupée par la porte
        );
        registerActor(toOrbWay); // <-- Enregistrez la porte !
    }
    @Override
    public String getTitle() { return "Spawn";}
}