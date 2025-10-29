package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.DOWN;
import static ch.epfl.cs107.play.math.Orientation.RIGHT;
import static ch.epfl.cs107.play.math.Orientation.UP;
import static ch.epfl.cs107.play.math.Orientation.LEFT;


public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity{

    private final static int MOVE_DURATION = 8;
    private final KeyBindings.PlayerKeyBindings keys;
    private final Element element; // L'élément servi par le joueur
    private OrientedAnimation currentAnimation;

    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Element element,  KeyBindings.PlayerKeyBindings keys) {
        super(owner, Orientation.DOWN, coordinates);
        this.element = element;
        this.keys = keys;
        final int ANIMATION_DURATION = 4;
        final Vector anchor = new Vector (0 , 0);
        final Orientation [] orders = {DOWN , RIGHT , UP , LEFT };
        this.currentAnimation = new OrientedAnimation(spriteName, ANIMATION_DURATION, this,
                anchor, orders, 4, 1, 2, 16, 32, true);
        resetMotion();
    }


    @Override
    public Element element() {
        return element;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(keys.left()));
        moveIfPressed(Orientation.UP, keyboard.get(keys.up()));
        moveIfPressed(Orientation.RIGHT, keyboard.get(keys.right()));
        moveIfPressed(Orientation.DOWN, keyboard.get(keys.down()));
        super.update(deltaTime);
        if (isDisplacementOccurs()) {
            currentAnimation.update(deltaTime);
        } else {
            currentAnimation.reset();
        }
    }


    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        currentAnimation.draw(canvas);
    }



    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false; // Changé pour être cohérent avec un jeu top-down classique
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // La logique d'interaction sera développée plus tard
    }

    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }
}