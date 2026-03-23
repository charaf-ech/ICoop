package ch.epfl.cs107.icoop;

import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.KeyBindings.PlayerKeyBindings;
import ch.epfl.cs107.play.areagame.AreaGame;
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

public class ICoop extends AreaGame {

    private final String[] areas = {"Spawn", "OrbWay"};
    private ICoopPlayer player1;
    private ICoopPlayer player2;
    private int areaIndex;

    private void createAreas() {
        addArea(new Spawn());
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

        // Normal Game Update
        super.update(deltaTime);

        // --- DEATH CHECK ---
        // If either player dies, reset the area automatically
        if (player1 != null && player2 != null) {
            if (player1.isDead() || player2.isDead()) {
                resetArea();
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

    /**
     * Checks if the player has lost all HP.
     * @return true if dead.
     */
}