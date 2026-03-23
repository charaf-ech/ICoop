package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    // Each interactWith variant receives the target and whether the interaction is cell-based (adjacent) or view-based (distance).
    default void interactWith(Door door, boolean isCellInteraction) {}
    default void interactWith(ICoopBehavior.ICoopCell cell, boolean isCellInteraction) {}
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {}
    default void interactWith(Obstacle obstacle, boolean isCellInteraction) {}
    default void interactWith(Rock rock, boolean isCellInteraction) {}
    default void interactWith(Explosive explosive, boolean isCellInteraction) {}
    default void interactWith(ElementalWall wall, boolean isCellInteraction) {}
    default void interactWith(Orb orb, boolean isCellInteraction) {}
}