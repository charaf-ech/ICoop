package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.engine.actor.TextGraphics;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.Signal;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.DOWN;
import static ch.epfl.cs107.play.math.Orientation.RIGHT;
import static ch.epfl.cs107.play.math.Orientation.UP;
import static ch.epfl.cs107.play.math.Orientation.LEFT;

/**
 * A ICoopPlayer is a player for the ICoop game.
 * It is an ElementalEntity.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor {

    private final static int MOVE_DURATION = 4;
    private final KeyBindings.PlayerKeyBindings keys;
    private final Element element; // Current elemental type served by this player
    private float hp;
    private OrientedAnimation currentAnimation;
    private final ICoopPlayerInteractionHandler handler;
    private final ICoop game;
    private final Health health;
    private int immunityTimer = 0;
    private static final int MAX_HEALTH = 5;
    private static final int IMMUNITY_DURATION = 24;

    /**
     * Default ICoopPlayer constructor
     * @param owner (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the player, not null
     * @param coordinates (DiscreteCoordinates): Initial position of the player, not null
     * @param spriteName (String): Name of the sprite, not null
     * @param element (Element): The element served by this player, not null
     */
    public ICoopPlayer(ICoop game, Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Element element, KeyBindings.PlayerKeyBindings keys) {
        super(owner, orientation, coordinates);
        this.game = game;
        this.element = element;
        this.keys = keys;
        this.handler = new ICoopPlayerInteractionHandler();
        this.health = new Health(this, Transform.I.translated(0, 1.75f), MAX_HEALTH, true);
        final Vector anchor = new Vector (0 , 0);
        final int ANIMATION_DURATION = 4;
        final Orientation [] orders = {DOWN , RIGHT , UP , LEFT };
        this.currentAnimation = new OrientedAnimation(spriteName, ANIMATION_DURATION, this, anchor, orders, 4, 1, 2, 16, 32, true);

        resetMotion();
    }

    // Implémentation de la méthode de l'interface ElementalEntity
    @Override
    public Element element() {
        return element;
    }

    @Override
    public void update(float deltaTime) {
        // 1. Manage Immunity Timer
        if (immunityTimer > 0) {
            immunityTimer--;
        }

        // 2. Standard Movement Logic
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(keys.left()));
        moveIfPressed(Orientation.UP, keyboard.get(keys.up()));
        moveIfPressed(Orientation.RIGHT, keyboard.get(keys.right()));
        moveIfPressed(Orientation.DOWN, keyboard.get(keys.down()));

        super.update(deltaTime);

        // 3. Animation Logic
        if (isDisplacementOccurs()) {
            currentAnimation.update(deltaTime);
        } else {
            currentAnimation.reset();
        }
    }

    /**
     * Deals damage to the player if they are not currently immune.
     * @param amount (int): Amount of HP to lose.
     * @param damageType (Element): The type of damage (PHYSICAL, FIRE, WATER).
     */
    public void takeDamage(int amount, Element damageType) {
        // 1. If currently immune, ignore the damage
        if (immunityTimer > 0) {
            return;
        }

        // 2. Reduce health
        health.decrease(amount);

        // 3. Trigger immunity period if damage was actually taken
        if (amount > 0) {
            immunityTimer = IMMUNITY_DURATION;
        }
    }

    /**
     * Orientate and Move this player in the given orientation if the given button is down
     */
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
        // Draw Character (Blinking effect if immune)
        // If timer is even (24, 22, 20...), draw. If odd (23, 21...), hide.
        if (immunityTimer % 2 == 0) {
            currentAnimation.draw(canvas);
        }

        // Draw Health Bar (Always visible!)
        health.draw(canvas);
    }

    /* ===================================================================
     * La suite des méthodes est copiée de GhostPlayer car la logique
     * de base (interaction, gestion de zone, etc.) est la même.
     ===================================================================*/

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() { return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())); }

    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return getOwnerArea().getKeyboard().get(keys.useItem()).isDown();}

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        // Delegate to the player-specific interaction handler.
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public boolean isViewInteractable() {
        return false; // Not targetable from a distance in top-down gameplay
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof  ICoopInteractionVisitor visitor)
            visitor.interactWith(this, isCellInteraction);

    }

    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    public void enterArea(Area area, DiscreteCoordinates position) {
        // Re-register, center camera, and reset motion when changing areas.
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }


    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Door door, boolean isCellInteraction) {
            if (isCellInteraction && door.isOpen()) {
                // Teleport players to the door's destination area and coordinates.
                String dest = door.getDestinationAreaName();
                List<DiscreteCoordinates> coords = door.getDestinationCoordinates();
                game.switchArea(dest, coords);

                resetMotion();
            }
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            // "useItem" interaction is a View Interaction (not Cell)
            if (!isCellInteraction) {
                explosive.prime();
            }
        }

        @Override
        public void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction) {}

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {}
    }
}