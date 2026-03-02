package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Explosive extends AreaEntity implements Interactor {

    private boolean isPrimed = false;
    private int countdown;
    private final ExplosiveInteractionHandler interactionHandler;

    // Animations
    private Animation bombAnimation;
    private Animation explosionAnimation;

    /**
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the Explosive. Not null
     * @param countdown (int): Ticks until explosion after priming
     */
    public Explosive(Area area, DiscreteCoordinates position, int countdown) {
        super(area, Orientation.UP, position);
        this.countdown = countdown;
        this.interactionHandler = new ExplosiveInteractionHandler();

        this.bombAnimation = new Animation("icoop/explosive", 2, 1, 1, this, 16, 16, 8, true);

        this.explosionAnimation = new Animation("icoop/explosion", 7, 1.5f, 1.5f, this, 32, 32, 2, false);
    }

    @Override
    public void update(float deltaTime) {
        if (isPrimed) {
            // Countdown only decreases after the explosive has been primed by the player.
            countdown--;
        }

        // Update whichever animation should currently be displayed.
        if (isExploding()) {
            explosionAnimation.update(deltaTime);
            // Remove actor once the one-shot explosion animation finishes.
            if (explosionAnimation.isCompleted()) {
                getOwnerArea().unregisterActor(this);
            }
        } else {
            bombAnimation.update(deltaTime);
        }

        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isExploding()) {
            explosionAnimation.draw(canvas);
        } else {
            bombAnimation.draw(canvas);
        }
    }

    public void prime() {
        if (!isPrimed) {
            // Player request primes the bomb once;
            // countdown starts ticking afterwards.
            isPrimed = true;
        }
    }

    public boolean isExploding() {
        // An explosion is in progress when the countdown reaches zero or below.
        return countdown <= 0;
    }

    @Override
    public boolean takeCellSpace() { return false; }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof ICoopInteractionVisitor visitor) {
            visitor.interactWith(this, isCellInteraction);
        }
    }

    // --- INTERACTOR LOGIC ---

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        // Returns the 4 adjacent cells (Up, Down, Left, Right)
        return List.of(
                getCurrentMainCellCoordinates().jump(Vector.Y),
                getCurrentMainCellCoordinates().jump(Vector.Y.mul(-1f)),
                getCurrentMainCellCoordinates().jump(Vector.X),
                getCurrentMainCellCoordinates().jump(Vector.X.mul(-1f))
        );
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        // Only ask to interact (destroy rocks) if we are currently exploding
        return isExploding();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    // --- HANDLER ---

    private class ExplosiveInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Rock other, boolean isCellInteraction) {
            // Destroy rock if interaction is View (distance) and bomb is exploding
            if (!isCellInteraction && isExploding()) {
                other.destroy();
            }
        }
    }
}