package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.icoop.area.ICoopArea;

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
    }
    @Override
    public String getTitle() { return "Spawn";}
}