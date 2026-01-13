package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class ICoop extends AreaGame implements DialogHandler {

    private final String[] areas = {"Spawn", "OrbWay"};
    private ICoopPlayer player;
    private ICoopPlayer player1;
    private int areaIndex;
    private CenterOfMass centerOfMass;
    private Dialog currentDialog;
    private Queue<Dialog> dialogQueue = new LinkedList<>();

    private void createAreas() {
        // Pour Spawn
        Spawn spawn = new Spawn();
        spawn.setDialogHandler(this); // "this" est ICoop, qui est un DialogHandler
        addArea(spawn);

        // Pour OrbWay
        OrbWay orbWay = new OrbWay();
        orbWay.setDialogHandler(this);
        addArea(orbWay);
    }

    @Override
    public void publish(Dialog dialog) {
        // On vide la file et on lance ce dialogue immédiatement (comportement par défaut)
        dialogQueue.clear();
        dialogQueue.add(dialog);
        startNextDialog();
    }

    @Override
    public void publish(String... dialogKeys) {
        // On ajoute toute la séquence dans la file d'attente
        for (String key : dialogKeys) {
            dialogQueue.add(new Dialog(key));
        }
        // Si aucun dialogue n'est en cours, on lance le premier
        if (currentDialog == null) {
            startNextDialog();
        }
    }

    private void startNextDialog() {
        if (!dialogQueue.isEmpty()) {
            currentDialog = dialogQueue.poll(); // Récupère et enlève le premier de la liste
        } else {
            currentDialog = null; // Plus rien à afficher, on ferme
        }
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
        Keyboard keyboard = getWindow().getKeyboard();

        if (currentDialog != null) {
            if (getCurrentArea() != null) {
                super.update(0);
            }

            currentDialog.update(deltaTime);
            currentDialog.draw(getWindow());

            // On vérifie si le texte a fini de s'afficher (isCompleted)
            if (currentDialog.isCompleted()) {
                // ET on attend que le joueur appuie sur la touche pour passer/fermer
                if (keyboard.get(KeyBindings.NEXT_DIALOG).isPressed()) {
                    startNextDialog();
                }
            }else {
                // Optionnel : Si le joueur appuie sur espace PENDANT le défilement,
                // on pourrait vouloir accélérer ou forcer la fin (si la méthode existe).
                // Sinon, ne rien mettre ici laisse le texte défiler à sa vitesse.
            }
            return;
        }

        // Reset du Jeu complet (Touche R)
        if (keyboard.get(KeyBindings.RESET_GAME).isPressed()) {
            begin(getWindow(), getFileSystem());
            return; // On arrête l'update ici pour ce tour
        }

        // Reset de l'Aire courante (Touche T) OU mort d'un joueur
        if (keyboard.get(KeyBindings.RESET_AREA).isPressed() || player.isDead() || player1.isDead()) {
            System.out.println("Joueur mort ! Reset de l'aire...");
            ICoopArea currentArea = (ICoopArea) getCurrentArea();

            // 1. On nettoie l'aire (supprime les acteurs)
            player.leaveArea();
            player1.leaveArea();
            currentArea.begin(getWindow(), getFileSystem()); // Réinitialise l'aire///

            // 2. On récupère les positions de spawn spécifiques à cette aire
            List<DiscreteCoordinates> spawnPositions = currentArea.getPlayerSpawnPosition();
            DiscreteCoordinates redSpawn = spawnPositions.get(0);
            DiscreteCoordinates blueSpawn = spawnPositions.get(1);

            // 3. On recrée les joueurs (pour réinitialiser leur état)
            player = new ICoopPlayer(currentArea, Orientation.DOWN, redSpawn, "icoop/player", Element.FIRE, KeyBindings.RED_PLAYER_KEY_BINDINGS);
            player1 = new ICoopPlayer(currentArea, Orientation.DOWN, blueSpawn, "icoop/player2", Element.WATER, KeyBindings.BLUE_PLAYER_KEY_BINDINGS);

            // 4. On les replace dans l'aire
            player.enterArea(currentArea, redSpawn);
            player1.enterArea(currentArea, blueSpawn);

            // 5. On replace la caméra
            centerOfMass = new CenterOfMass(player, player1);
            currentArea.registerActor(centerOfMass);
            currentArea.setViewCandidate(centerOfMass);

            return; // On arrête l'update ici
        }
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
