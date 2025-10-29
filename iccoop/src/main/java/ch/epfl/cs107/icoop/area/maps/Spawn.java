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
        ArrayList<DiscreteCoordinates> OrbWayCoords = new ArrayList<>();
        OrbWayCoords.add(new DiscreteCoordinates(1,12));
        OrbWayCoords.add(new DiscreteCoordinates(1,5));
        Door door1 = new Door("OrbWay", Logic.TRUE,OrbWayCoords,this,new DiscreteCoordinates(19,15));
    }
    @Override
    public String getTitle() { return "Spawn";}
}