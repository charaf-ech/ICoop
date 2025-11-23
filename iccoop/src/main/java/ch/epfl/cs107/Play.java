package ch.epfl.cs107;

import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.play.engine.Game;
import ch.epfl.cs107.play.io.DefaultFileSystem;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.io.ResourceFileSystem;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.window.swing.SwingWindow;

public class Play {

    private static final float ONE_SEC = 1E9f;
	public static final int WINDOW_HEIGHT = 550;
	public static final int WINDOW_WIDTH = 550;


	public static void main(String[] args) {
		final FileSystem fileSystem = new ResourceFileSystem(DefaultFileSystem.INSTANCE);

		final Game game = new ICoop();

		final Window window = new SwingWindow(game.getTitle(), fileSystem, WINDOW_WIDTH, WINDOW_HEIGHT);
		window.registerFonts(ResourcePath.FONTS);

		try {
			if (game.begin(window, fileSystem)) {
                long currentTime = System.nanoTime();
				long lastTime;
				final float frameDuration = ONE_SEC / game.getFrameRate();

				while (!window.isCloseRequested()) {

                    lastTime = currentTime;
                    currentTime = System.nanoTime();
					float deltaTime = (currentTime - lastTime);

                    try {
                        int timeDiff = Math.max(0, (int) (frameDuration - deltaTime));
                        Thread.sleep((int) (timeDiff / 1E6), (int) (timeDiff % 1E6));
                    } catch (InterruptedException e) {
                        System.out.println("Thread sleep interrupted");
                    }

                    currentTime = System.nanoTime();
                    deltaTime = (currentTime - lastTime) / ONE_SEC;

                    game.update(deltaTime);

					game.draw();
                    window.update();
				}
			}
			game.end();
		} finally {
			window.dispose();
		}
	}
}