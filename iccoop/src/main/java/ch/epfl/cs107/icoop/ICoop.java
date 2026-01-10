package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Actor;
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
    private CenterOfMass centerOfMass;

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
            player.enterArea(spawnArea, spawnPosition1);
            centerOfMass = new CenterOfMass(player, player1);
            spawnArea.registerActor(centerOfMass);
            spawnArea.setViewCandidate(centerOfMass); // La caméra suit le centre de masse
            return true;
        }
        return false;
    }
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // Important !

        float distance = player.getCurrentMainCellCoordinates().distanceBetween(player.getCurrentMainCellCoordinates() , player1.getCurrentMainCellCoordinates());
        float scaleFactor = Math.max(13, 13 * 0.75f + distance / 2);
        Door crossedDoor = player.getCrossedDoor();

        if (crossedDoor == null) {
            crossedDoor = player1.getCrossedDoor();
        }

        if (crossedDoor != null) {

            player.leaveArea();
            player1.leaveArea();


            ICoopArea nextArea = (ICoopArea) setCurrentArea(crossedDoor.getDestination(), false);

            player.enterArea(nextArea, crossedDoor.getArrivalCoordinates(player.element()));
            player1.enterArea(nextArea, crossedDoor.getArrivalCoordinates(player1.element()));

            centerOfMass = new CenterOfMass(player, player1); // On crée un centre de masse pour la nouvelle aire
            nextArea.registerActor(centerOfMass);             // On l'enregistre dans l'aire
            nextArea.setViewCandidate(centerOfMass);          // On dit à la caméra de le suivre
            // ----------------------------------------------------

            player.resetCrossedDoor();
            player1.resetCrossedDoor();
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
