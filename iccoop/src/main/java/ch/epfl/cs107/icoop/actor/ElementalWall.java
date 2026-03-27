package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class ElementalWall extends AreaEntity {

    private final Element element;
    private final Sprite sprite;

    public ElementalWall(Area area, Orientation orientation, DiscreteCoordinates position, Element element) {
        super(area, orientation, position);
        this.element = element;
        String spriteName = (element == Element.FIRE) ? "fire_wall" : "water_wall";
        // BINGO! Using the GitHub logic: 4 frames, 256x256 pixels.
        Sprite[] wallSprites = RPGSprite.extractSprites(spriteName, 4, 1f, 1f, this, Vector.ZERO, 256, 256);
        // Choose the correct image based on the direction the wall is facing
        this.sprite = wallSprites[orientation.ordinal()];
    }

    public Element getElement() {
        return element;
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
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