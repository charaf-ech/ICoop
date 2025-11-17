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
import ch.epfl.cs107.play.window.Window;


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

            // --- Récupérer les deux positions de spawn ---
            DiscreteCoordinates spawnPosition1 = currentArea.getPlayerSpawnPosition().get(0);
            DiscreteCoordinates spawnPosition2 = currentArea.getPlayerSpawnPosition().get(1);

            // 1. Créer le Joueur Rouge (Player 1)
            PlayerKeyBindings keys1 = KeyBindings.RED_PLAYER_KEY_BINDINGS;
            player1 = new ICoopPlayer(currentArea, Orientation.DOWN, spawnPosition1,
                    "icoop/player", Element.FIRE, keys1);

            // 2. Créer le Joueur Bleu (Player 2)
            PlayerKeyBindings keys2 = KeyBindings.BLUE_PLAYER_KEY_BINDINGS;
            player2 = new ICoopPlayer(currentArea, Orientation.DOWN, spawnPosition2,
                    "icoop/player2", Element.WATER, keys2);
            // Note: L'élément WATER est attribué par convention

            // 3. Enregistrer et centrer la caméra
            // NOTE: Le centrage de la caméra doit se faire sur un CenterOfMass (Tâche 2.5)
            // Mais pour l'instant, on laisse le centrage sur un joueur :

            player1.enterArea(currentArea, spawnPosition1);
            player2.enterArea(currentArea, spawnPosition2);

            // Remplacer ces deux lignes par l'utilisation de CenterOfMass (Tâche 2.5)
            // Pour l'instant, seul le dernier appel est effectif:
            // player1.centerCamera();
            // player2.centerCamera();

            return true;
        }
        return false;
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

    @Override
    public void update(float deltaTime) {
        if (player.isWeak())
            switchArea();
        super.update(deltaTime);
    }

    private void switchArea() {
        player.leaveArea();
        areaIndex = (areaIndex == 0) ? 1 : 0;
        ICoopArea currentArea = (ICoopArea) setCurrentArea(areas[areaIndex], false);
        player.enterArea(currentArea, currentArea.getPlayerSpawnPosition());
        player.strengthen();
    }*/
}
