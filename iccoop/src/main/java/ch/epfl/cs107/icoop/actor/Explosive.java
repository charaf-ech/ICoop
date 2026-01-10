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
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Explosive extends AreaEntity implements Interactor {

    private final Animation explosionAnimation;
    private final Animation idleAnimation;
    private int fuseTimer; // Compte à rebours
    private boolean isActivated;
    private boolean isExploded;

    public Explosive(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.fuseTimer = 20; // Durée avant explosion (ajustable)
        this.isActivated = false;
        this.isExploded = false;

        // Animations selon l'énoncé/annexe
        this.idleAnimation = new Animation("icoop/explosive", 2, 1, 1, this, 16, 16, 2, true);
        this.explosionAnimation = new Animation("icoop/explosion", 7, 1, 1, this, 32, 32, 3, false);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isExploded) {
            explosionAnimation.update(deltaTime);
            if (explosionAnimation.isCompleted()) {
                getOwnerArea().unregisterActor(this); // Disparaît après l'anim
            }
        } else if (isActivated) {
            idleAnimation.update(deltaTime);
            fuseTimer--;
            if (fuseTimer <= 0) {
                explode();
            }
        } else {
            idleAnimation.update(deltaTime);
        }
    }

    private void explode() {
        isExploded = true;
        // L'explosion peut faire trembler l'écran ou faire du bruit ici si voulu
    }

    public void activate() {
        if (!isActivated && !isExploded) {
            this.isActivated = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (isExploded) {
            explosionAnimation.draw(canvas);
        } else {
            idleAnimation.draw(canvas);
        }
    }

    // --- Gestion des Interactions ---

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return true; // Traversable
    }

    @Override
    public boolean isCellInteractable() {
        return !isActivated && !isExploded; // Interactable si pas encore active
    }

    @Override
    public boolean isViewInteractable() {
        return !isActivated && !isExploded; // Interactable à distance (pour l'activer)
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    // --- En tant qu'Interactor (quand elle explose) ---

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        // Retourne les 4 voisins directs
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return isExploded; // Ne détruit les murs que si elle explose
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new ICoopExplosiveHandler(), isCellInteraction);
    }

    // Handler interne pour gérer ce que l'explosion détruit
    private class ICoopExplosiveHandler implements ICoopInteractionVisitor {
        public void interactWith(Rock rock, boolean isCellInteraction) {
            if (!isCellInteraction) { // Destruction à distance (via FOV)
                rock.destroy();
            }
        }
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            // L'explosif blesse le joueur s'il est dans la zone d'explosion (vue ou contact)
            // L'énoncé suggère des dégâts fixes, par exemple 2 points.
            player.takeDamage(2);
        }
    }
}