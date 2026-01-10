package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {


    default void interactWith(Door door, boolean isCellInteraction) {
    }


    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {
    }

    default void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction) {
    }

    default void interactWith(Obstacle obstacle, boolean isCellInteraction){
    }

    default void interactWith(Rock rock, boolean isCellInteraction) {
        // Par défaut, rien
    }

    default void interactWith(Explosive explosive, boolean isCellInteraction) {
        // Par défaut, rien
    }
}