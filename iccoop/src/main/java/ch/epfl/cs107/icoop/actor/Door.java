package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;


import java.util.ArrayList;
import java.util.List;

// 1. Ajoutez "implements Interactable"
public class Door extends AreaEntity implements Interactable {

    private final String destination;
    private final DiscreteCoordinates[] arrivalCoordinates;
    private final Logic signal;
    private final List<DiscreteCoordinates> otherCells; // Pour stocker les cases en plus

    /**
     * Constructeur pour la porte
     * @param destination Nom de l'aire de destination
     * @param signal Signal pour savoir si la porte est ouverte
     * @param owner L'aire
     * @param mainCellPosition Case principale
     * @param otherCellPositions Autres cases occupées par la porte
     * @param arrivalCoordinates Coordonnées d'arrivée (pour rouge et bleu)
     */
    public Door(String destination, Logic signal, Area owner, DiscreteCoordinates mainCellPosition, List<DiscreteCoordinates> otherCellPositions, DiscreteCoordinates... arrivalCoordinates) {
        // 2. On utilise le constructeur que vous avez
        super(owner, Orientation.DOWN, mainCellPosition);
        this.destination = destination;
        this.arrivalCoordinates = arrivalCoordinates;
        this.signal = signal;
        this.otherCells = otherCellPositions; // On stocke les autres cases
    }

    /**
     * Constructeur simplifié (demandé par l'énoncé)
     */
    public Door(String destination, Logic signal, Area owner, DiscreteCoordinates mainCellPosition, DiscreteCoordinates[] arrivalCoordinates, DiscreteCoordinates... otherCellPositions) {
        this(destination, signal, owner, mainCellPosition, List.of(otherCellPositions), arrivalCoordinates);
    }

    // --- Getters ---

    public String getDestination() {
        return destination;
    }

    public DiscreteCoordinates getArrivalCoordinates(Element playerElement) {
        if (playerElement == Element.FIRE && arrivalCoordinates.length > 0) {
            return arrivalCoordinates[0]; //
        } else if (playerElement == Element.WATER && arrivalCoordinates.length > 1) {
            return arrivalCoordinates[1]; //
        }
        return arrivalCoordinates[0]; // Sécurité
    }

    public boolean isOn() {
        return signal.isOn(); //
    }

    // --- Méthodes de AreaEntity / Interactable ---

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        // 3. C'est la correction la plus importante !
        // On retourne la case principale (de AreaEntity) ET les autres
        List<DiscreteCoordinates> allCells = new ArrayList<>();
        allCells.add(getCurrentMainCellCoordinates());
        allCells.addAll(otherCells);
        return allCells;
    }

    @Override
    public void draw(Canvas canvas) {
        // Doit être vide, la porte est invisible
    }

    @Override
    public boolean takeCellSpace() {
        return false; // Traversable
    }

    @Override
    public boolean isCellInteractable() {
        return true; // Accepte les interactions de contact
    }

    @Override
    public boolean isViewInteractable() {
        return false; // N'accepte pas les interactions à distance
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // Appelle le visiteur (maintenant que l'interface est corrigée)
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}