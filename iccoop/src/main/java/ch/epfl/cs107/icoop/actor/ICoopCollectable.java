package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public abstract class ICoopCollectable extends CollectableAreaEntity {

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    @Override
    public boolean takeCellSpace() {
        return false; // Traversable
    }

    @Override
    public boolean isViewInteractable() {
        return false; // Par défaut, interaction de contact uniquement
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // La gestion concrète se fera dans les sous-classes ou via le visiteur
        // Pour l'instant, on peut laisser le comportement par défaut ou délégue
    }
}