package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Orb extends AreaEntity {

    private final Element element;
    private final Animation animation;
    private boolean isCollected = false;

    public Orb(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);
        this.element = element;

        // --- THE GENIUS ANIMATION MATH YOU FOUND ---
        final int ANIMATION_FRAMES = 6;
        int spriteYDelta = (element == Element.FIRE) ? 64 : 0; // Fire is at Y=64, Water is at Y=0

        Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            // Cut out 6 squares of 32x32 pixels from left to right
            sprites[i] = new Sprite("icoop/orb", 1f, 1f, this,
                    new RegionOfInterest(i * 32, spriteYDelta, 32, 32));
        }

        // Create the animation (4 ticks per frame)
        this.animation = new Animation(4, sprites);
    }

    public Element getElement() {
        return element;
    }

    public void collect() {
        isCollected = true;
        getOwnerArea().unregisterActor(this);
    }

    public boolean isCollected() {
        return isCollected;
    }

    @Override
    public void update(float deltaTime) {
        if (!isCollected) {
            animation.update(deltaTime);
        }
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected) {
            animation.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false; // Walk over it to collect
    }

    @Override
    public boolean isCellInteractable() {
        return !isCollected;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof ICoopInteractionVisitor visitor) {
            visitor.interactWith(this, isCellInteraction);
        }
    }
}