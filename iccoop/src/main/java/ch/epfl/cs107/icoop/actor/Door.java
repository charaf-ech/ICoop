package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.Signal;

import java.util.List;

public class Door extends AreaEntity {

    private final String destinationAreaName;
    private final List<DiscreteCoordinates> destinationCoordinates;
    private final Signal signal;
    private final List<DiscreteCoordinates> occupiedCells;

    public Door(Area area, String destinationAreaName, List<DiscreteCoordinates> destinationCoordinates,
                Orientation orientation, DiscreteCoordinates position, List<DiscreteCoordinates> occupiedCells, Signal signal) {
        super(area, orientation, position);
        this.destinationAreaName = destinationAreaName;
        this.destinationCoordinates = destinationCoordinates;
        this.signal = signal;
        this.occupiedCells = occupiedCells;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells(){ return occupiedCells; }

    @Override
    public boolean takeCellSpace() { return false; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof  ICoopInteractionVisitor visitor) {
            visitor.interactWith(this, isCellInteraction);
        }
    }
}