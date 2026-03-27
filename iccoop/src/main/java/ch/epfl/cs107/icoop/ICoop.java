package ch.epfl.cs107.icoop;

import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.KeyBindings.PlayerKeyBindings;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.List;

public class ICoop extends AreaGame implements DialogHandler {

    private final String[] areas = {"Spawn", "OrbWay"};
    private ICoopPlayer player1;
    private ICoopPlayer player2;
    private int areaIndex;
    private Dialog activeDialog;

    private void createAreas() {
        addArea(new Spawn(this)); // "this" est ICoop, qui est un DialogHandler !
        addArea(new OrbWay());
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            ICoopArea currentArea = (ICoopArea) setCurrentArea("Spawn", true);

            // --- Get Spawn Positions ---
            DiscreteCoordinates spawnPosition1 = currentArea.getPlayerSpawnPosition().get(0);
            DiscreteCoordinates spawnPosition2 = currentArea.getPlayerSpawnPosition().get(1);

            //  Create Red Player (Player 1)
            PlayerKeyBindings keys1 = KeyBindings.RED_PLAYER_KEY_BINDINGS;
            player1 = new ICoopPlayer(this, currentArea, Orientation.DOWN, spawnPosition1,
                    "icoop/player", Element.FIRE, keys1);

            //  Create Blue Player (Player 2)
            PlayerKeyBindings keys2 = KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
            player2 = new ICoopPlayer(this, currentArea, Orientation.DOWN, spawnPosition2,
                    "icoop/player2", Element.WATER, keys2);

            //  Register players in the area
            // Note: enterArea() in ICoopPlayer sets the camera on that player.
            // So the camera will snap to player2 since they enter last.
            player1.enterArea(currentArea, spawnPosition1);
            player2.enterArea(currentArea, spawnPosition2);

            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getWindow().getKeyboard();

        // --- RESET GAME ('R') ---
        if (keyboard.get(KeyBindings.RESET_GAME).isDown()) {
            end();
            begin(getWindow(), getFileSystem());
        }
        // --- RESET AREA ('T') ---
        if (keyboard.get(KeyBindings.RESET_AREA).isDown()) {
            resetArea();
        }

        // --- MANAGE ACTIVE DIALOG ---
        if (activeDialog != null) {
            // If the user presses the NEXT_DIALOG key, advance the text
            if (keyboard.get(KeyBindings.NEXT_DIALOG).isPressed()) {
                activeDialog.update(deltaTime);
            }

            // If the text is completely finished, remove the dialog
            if (activeDialog.isCompleted()) {
                activeDialog = null;
            }
        }
        // --- NORMAL GAME UPDATE (Only happens if NO dialog is active) ---
        else {
            super.update(deltaTime);

            // --- DEATH CHECK ---
            if (player1 != null && player2 != null) {
                if (player1.isDead() || player2.isDead()) {
                    resetArea();
                }
            }
        }
    }

    /**
     * Helper method to reset the current area and respawn players.
     */
    private void resetArea() {
        // 1. Remove players from the area
        player1.leaveArea();
        player2.leaveArea();

        // 2. Reset the area (recreates obstacles)
        ICoopArea currentArea = (ICoopArea) getCurrentArea();
        currentArea.begin(getWindow(), getFileSystem());

        // 3. Reset Health (Crucial for Death!)
        player1.resetHealth();
        player2.resetHealth();

        // 4. Respawn players at the starting positions
        DiscreteCoordinates spawn1 = currentArea.getPlayerSpawnPosition().get(0);
        DiscreteCoordinates spawn2 = currentArea.getPlayerSpawnPosition().get(1);

        player1.enterArea(currentArea, spawn1);
        player2.enterArea(currentArea, spawn2);
    }

    @Override
    public void end() { }

    @Override
    public String getTitle() {
        return "ICoop";
    }

    /**
     * Handles the transition between areas for both players
     */
    public void switchArea(String areaKey, List<DiscreteCoordinates> spawnPositions) {
        player1.leaveArea();
        player2.leaveArea();

        ICoopArea currentArea = (ICoopArea) setCurrentArea(areaKey, false);

        player1.enterArea(currentArea, spawnPositions.get(0));
        player2.enterArea(currentArea, spawnPositions.get(1));
    }

    @Override
    public void publish(Dialog dialog) {
        this.activeDialog = dialog;
    }

    @Override
    public void draw() {
        super.draw(); // Dessine la carte et les joueurs

        // Dessine le texte par-dessus tout le reste
        if (activeDialog != null) {
            activeDialog.draw(getWindow());
        }
    }
}