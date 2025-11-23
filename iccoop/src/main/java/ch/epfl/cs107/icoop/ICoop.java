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
// Note: Adaptez 13 selon votre DEFAULT_SCALE_FACTOR
        // 1. On vérifie si LE JOUEUR ROUGE a touché une porte
        Door crossedDoor = player.getCrossedDoor();

        // 2. Si ce n'est pas le cas, on vérifie si LE JOUEUR BLEU a touché une porte
        if (crossedDoor == null) {
            crossedDoor = player1.getCrossedDoor();
        }

        // 3. Si une porte a été trouvée (activée par l'un des deux)
        if (crossedDoor != null) {

            // A. Les DEUX joueurs quittent l'aire
            player.leaveArea();
            player1.leaveArea();

            // B. On change l'aire courante
            ICoopArea nextArea = (ICoopArea) setCurrentArea(crossedDoor.getDestination(), false);

            // C. Les DEUX joueurs entrent dans la nouvelle aire
            // Note : getArrivalCoordinates utilise l'élément du joueur pour donner la bonne position (voir votre classe Door)
            player.enterArea(nextArea, crossedDoor.getArrivalCoordinates(player.element()));
            player1.enterArea(nextArea, crossedDoor.getArrivalCoordinates(player1.element()));

            // D. Gestion de la caméra (voir point suivant sur CenterOfMass)
            player.centerCamera();

            // E. On réinitialise la porte pour les DEUX (pour éviter une boucle infinie)
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
