package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.engine.actor.TextGraphics;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
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
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity {

    private final static int MOVE_DURATION = 8;
    private final TextGraphics message;
    private final Sprite sprite;
    private final Element element; // L'élément servi par le joueur
    private float hp;
    private OrientedAnimation currentAnimation;
    final Vector anchor = new Vector (0 , 0);
    final Orientation [] orders = {DOWN , RIGHT , UP , LEFT };
    /**
     * Default ICoopPlayer constructor
     * @param owner (Area): Owner area, not null
     * @param orientation (Orientation): Initial orientation of the player, not null
     * @param coordinates (DiscreteCoordinates): Initial position of the player, not null
     * @param spriteName (String): Name of the sprite, not null
     * @param element (Element): The element served by this player, not null
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Element element) {
        super(owner, orientation, coordinates);
        this.element = element; // Initialisation de l'élément
        this.hp = 10;

        message = new TextGraphics(Integer.toString((int) hp), 0.4f, Color.BLUE);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));

        sprite = new Sprite(spriteName, 1.f, 1.f, this);
        resetMotion();
    }

    // Implémentation de la méthode de l'interface ElementalEntity
    @Override
    public Element element() {
        return element;
    }

    @Override
    public void update(float deltaTime) {
        // Logique de points de vie (similaire à GhostPlayer)
        if (hp > 0) {
            hp -= deltaTime;
            message.setText(Integer.toString((int) hp));
        }
        if (hp < 0) hp = 0.f;

        // Logique de mouvement (similaire à GhostPlayer)
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(DOWN, keyboard.get(Keyboard.DOWN));

        super.update(deltaTime);
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
        currentAnimation.draw(canvas);
        message.draw(canvas);
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

    public boolean isWeak() {
        return (hp <= 0.f);
    }

    public void strengthen() {
        hp = 10;
    }

    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }
}