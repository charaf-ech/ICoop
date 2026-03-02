package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Obstacle extends AreaEntity {

    private final Sprite sprite;

    public Obstacle(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        // Default sprite for normal Obstacles is "rock.2"
        this.sprite = new Sprite("rock.2", 1, 1.f, this);
    }

    // --- PROTECTED CONSTRUCTOR FOR CHILDREN (ROCK) ---
    // Allows Rock to define its own sprite name
    protected Obstacle(Area area, Orientation orientation, DiscreteCoordinates position, String spriteName) {
        super(area, orientation, position);
        this.sprite = new Sprite(spriteName, 1, 1.f, this);
    }

    @Override
    public void draw(Canvas canvas) { sprite.draw(canvas); }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() { return true; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return true; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof ICoopInteractionVisitor visitor) {
            visitor.interactWith(this, isCellInteraction);
        }
    }
}