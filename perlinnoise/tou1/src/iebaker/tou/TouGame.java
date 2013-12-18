package iebaker.tou;

import iebaker.krypton.core.Application;
import iebaker.krypton.core.Screen;

import iebaker.tou.screens.TouMainScreen;

import cs195n.*;

public class TouGame {
	public static void main(String... args) {
		Application tou_game = new Application("Tou", false, new Vec2i(650, 800));
		tou_game.getScreenManager().pushScreen(new TouMainScreen(tou_game, "tou.main"));
		tou_game.startup();
	}
}
