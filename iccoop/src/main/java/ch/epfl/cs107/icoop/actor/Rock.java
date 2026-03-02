package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Rock extends Obstacle {

    private boolean isDestroyed = false;

    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        // Use the protected constructor to set image "rock.1"
        super(area, orientation, position, "rock.1");
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isDestroyed) {
            super.draw(canvas);
        }
    }

    @Override
    public boolean takeCellSpace() {
        // Blocks movement until destroyed; then becomes walkable.
        return !isDestroyed;
    }

    @Override
    public boolean isViewInteractable() {
        // Only interactable while the rock exists.
        return !isDestroyed;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (!isDestroyed && v instanceof ICoopInteractionVisitor visitor) {
            visitor.interactWith(this, isCellInteraction);
        }
    }

    // This will be called later by the Explosive
    public void destroy() {
        isDestroyed = true;
    }
}