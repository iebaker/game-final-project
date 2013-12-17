package iebaker.tac;

import iebaker.tac.screens.TacMainScreen;
import iebaker.argon.core.Application;
import cs195n.*;

/**
 * Class which contains the main method of TacGame.  Sets up an application and runs it.
 */
public class TacGame {
	public static void main(String... args) {
		Application tac_game = new Application("Tac", false, new Vec2i(800, 650));
		tac_game.getScreenManager().pushScreen(new TacMainScreen(tac_game, "tac.main"));
		tac_game.startup();
	}
}
