package ch.epfl.cs107.icoop;


import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame {
    private final String[] areas = {"Spawn", "Orbway"};
    private ICoopPlayer player;
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
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }
    @Override
    public void update(float deltaTime) {
        if (player.isWeak())
            switchArea();
        super.update(deltaTime);
    }
    @Override
    public void end() {

    }
    @Override
    public String getTitle() {
        return "ICoop";
    }
    private void initArea(String areaKey) {
        Tuto2Area area = (Tuto2Area) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        player = new GhostPlayer(area, Orientation.DOWN, coords, "ghost.1");
        player.enterArea(area, coords);
        player.centerCamera();
    }
    private void switchArea() {
        player.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        Tuto2Area currentArea = (Tuto2Area) setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
        player.strengthen();
    }
}
