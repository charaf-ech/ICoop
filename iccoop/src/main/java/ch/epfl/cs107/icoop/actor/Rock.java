package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public class Rock extends Obstacle {

    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, "rock.1"); // Image spécifique rock.1
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    // Méthode pour détruire le rocher
    public void destroy() {
        getOwnerArea().unregisterActor(this);
    }
}