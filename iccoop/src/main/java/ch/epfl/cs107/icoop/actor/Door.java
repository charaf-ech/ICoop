package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.List;

public class Door extends AreaEntity {

    private final String destination;
    private final ArrayList<DiscreteCoordinates> arrivalCoordinates;
    private Logic signal;
    public Door(String destination, Logic signal, ArrayList<DiscreteCoordinates> arrivalCoordinates,
                Area area, DiscreteCoordinates position) {

        //we do not draw the door, so the orientation is arbitrary
        super(area, Orientation.DOWN, position);
        this.destination = destination;
        this.signal = signal;
        this.arrivalCoordinates = arrivalCoordinates;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of();
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }
}