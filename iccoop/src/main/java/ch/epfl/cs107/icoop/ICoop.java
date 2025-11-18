package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame {

    private final String[] areas = {"Spawn", "OrbWay"};
    private ICoopPlayer player;
    private ICoopPlayer player1;
    private int areaIndex;

    private void createAreas() {
        addArea(new Spawn());
        addArea(new OrbWay());
    }
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            ICoopArea spawnArea = (ICoopArea) setCurrentArea("Spawn", true);
            DiscreteCoordinates spawnPosition1 = new DiscreteCoordinates(13, 6);
            DiscreteCoordinates spawnPosition2 = new DiscreteCoordinates(12, 6);
            player1 = new ICoopPlayer(spawnArea,Orientation.DOWN,spawnPosition2,"icoop/player2",Element.WATER,KeyBindings.BLUE_PLAYER_KEY_BINDINGS);
            player = new ICoopPlayer(spawnArea, Orientation.DOWN, spawnPosition1, "icoop/player", Element.FIRE, KeyBindings.RED_PLAYER_KEY_BINDINGS);
            player1.enterArea(spawnArea, spawnPosition2);
            player1.centerCamera();
            player.enterArea(spawnArea, spawnPosition1);
            player.centerCamera();
            return true;
        }
        return false;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Important !

        // Logique de transition (Tâche 2.4.3)
        Door crossedDoor = player.getCrossedDoor();
        if (crossedDoor != null) {
            // 1. Le joueur quitte l'aire actuelle
            player.leaveArea();

            // 2. On récupère la nouvelle aire
            ICoopArea nextArea = (ICoopArea) setCurrentArea(crossedDoor.getDestination(), false);

            // 3. On calcule la position d'arrivée
            DiscreteCoordinates arrival = crossedDoor.getArrivalCoordinates(player.element());

            // 4. Le joueur entre dans la nouvelle aire
            player.enterArea(nextArea, arrival);
            player.centerCamera();

            // 5. On réinitialise l'état de la porte
            player.resetCrossedDoor();
        }
    }
    @Override
    public void end() {

    }
    @Override
    public String getTitle() {
        return "ICoop";
    }
    /*
    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        player = new ICoopPlayer(area, Orientation.DOWN, coords, "ghost.1");
        player.enterArea(area, coords);
        player.centerCamera();
    }
    private void switchArea() {
        player.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICoopArea currentArea = (ICoopArea) setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
        player.strengthen();
    }*/
}
