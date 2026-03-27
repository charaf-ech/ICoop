package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.engine.actor.Dialog;

public interface DialogHandler {
    /**
     * Publie un dialogue pour qu'il devienne le dialogue actif du jeu.
     * @param dialog (Dialog): Le dialogue à afficher.
     */
    void publish(Dialog dialog);

}