package ch.epfl.cs107.icoop;

import static ch.epfl.cs107.play.window.Keyboard.*;


public final class KeyBindings {


public static final PlayerKeyBindings RED_PLAYER_KEY_BINDINGS = new PlayerKeyBindings(UP, LEFT, DOWN, RIGHT, J, K);


    public static final PlayerKeyBindings BLUE_PLAYER_KEY_BINDINGS = new PlayerKeyBindings(W, A, S, D, Q, E);

    public static final int NEXT_DIALOG = SPACE;

    public static final int RESET_GAME = R;

    public static final int RESET_AREA = T;

    private KeyBindings() {

    }


    public record PlayerKeyBindings(int up, int left, int down, int right, int switchItem, int useItem) {
    }
}
