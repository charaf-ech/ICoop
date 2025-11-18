package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer; // <-- Importez ICoopPlayer
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {

    /**
     * Interaction avec une porte
     * @param door la porte
     * @param isCellInteraction vrai si interaction de contact
     */
    default void interactWith(Door door, boolean isCellInteraction) {
        // Laisser vide par défaut
    }

    /**
     * Interaction avec un joueur
     * @param player le joueur
     * @param isCellInteraction vrai si interaction de contact
     */
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {
        // Laisser vide par défaut
    }
}