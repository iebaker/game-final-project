package smt3.m;

import cs195n.Vec2i;
import smt3.gameengine.ui.Application;

public class MMain {

	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		Application a = new Application("M", false, new Vec2i(500, 750));
		WelcomeScreen ws = new WelcomeScreen(a);
		a.startup();
		a.addScreen(ws);
	}

}
