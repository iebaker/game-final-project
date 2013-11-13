package iebaker.wt;

import iebaker.xenon.core.Application;
import iebaker.xenon.core.Screen;

import iebaker.wt.screens.WTMainScreen;

import cs195n.Vec2i;

public class WorkTogetherGame {
	public static void main(String... args) {
		//System.out.println("It's running...");
		Application wt_game = new Application("Work Together", false, new Vec2i(800, 800));
		wt_game.getScreenManager().pushScreen(new WTMainScreen(wt_game, "wt.mainscreen"));
		wt_game.startup();
	}
}
