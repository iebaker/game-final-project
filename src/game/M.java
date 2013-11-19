package game;

import engine.Application;
import engine.sound.SoundHolder;
import game.screens.MainScreen;

/**
 * Top level class for the M Game, supporting starting the game and nothing else
 * 
 * @author dgattey
 * 
 */
public class M {
	
	public static final String gameName = "Light";
	/**
	 * Creates a new Application and starts up the app with the first screen being a new MenuScreen
	 * 
	 * @param args
	 *            The arguments passed in (ignored)
	 */
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		Application a = new Application(gameName, false);
		a.pushScreen(new MainScreen(a));
		a.startup();
		new SoundHolder("lib/sounds.xml");
	}
	
}
