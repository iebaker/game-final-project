package iebaker.xenon.core;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * ScreenManager is a class which exposes functionality for manipulating the stack (list) of screens owned by the parent 
 * Application.  The ScreenManager has a reference to the parent application, as well as the parent's list of screens.
 * The ScreenManager also maintains a dictionary which indexes all the currently owned screens by their ScreenID attribute
 * so that a single screen can be retrieved by name if desired.  Pushing/Pumping a screen adds it to the dictionary.  Popping/
 * dropping a screen removes it from the dictionary.
 */
public class ScreenManager {
	private java.util.List<Screen> my_screens;
	private java.util.List<ScreenAction> my_actions;
	private java.util.Map<String, Screen> my_screen_dict;
	private Application my_app;

	public enum Op {
		ROTATE_UP, ROTATE_DOWN, CLEAR_SCREENS,
		POP_SCREEN,	DROP_SCREEN, REMOVE_SCREEN,
		PUSH_SCREEN, PUMP_SCREEN, SWAP_SCREENS,
	}

	private class ScreenAction {
		public Screen screen = null;
		public int index1 = -1;
		public int index2 = -1;
		public Op op = null;

		public ScreenAction(Op o) { op = o; }
		public ScreenAction(Op o, Screen s) { op = o; screen = s; }
		public ScreenAction(Op o, int i1, int i2) { op = o; index1 = i1; index2 = i2; }
	}

	/**
	 * Constructor.  Takes a reference to the parent application, as 
	 * well as a reference to the parent applications's list of screens.
	 *
	 * @param a 	a reference to the parent application
	 * @param s 	the parent application's list of screens
	 */
	public ScreenManager(Application a, java.util.List<Screen> s) {
		my_app = a;
		my_screens = s;
		my_screen_dict = new HashMap<String, Screen>();
		my_actions = new ArrayList<ScreenAction>();
	}

	public void update() {
		for(ScreenAction sa : my_actions) {
			switch(sa.op) {
				case ROTATE_UP:
					my_screens.add(0, my_screens.remove(my_screens.size() - 1));
					break;

				case ROTATE_DOWN:
					my_screens.add(my_screens.size() - 1, my_screens.remove(0));
					break;

				case POP_SCREEN:
					Screen popped = my_screens.remove(my_screens.size() - 1);
					my_screen_dict.remove(popped.attrScreenID);
					break;

				case DROP_SCREEN:
					Screen dropped = my_screens.remove(0);
					my_screen_dict.remove(dropped.attrScreenID);
					break;

				case REMOVE_SCREEN:
					my_screen_dict.remove(sa.screen.attrScreenID);
					my_screens.remove(sa.screen);
					break;

				case PUSH_SCREEN:
					my_screen_dict.put(sa.screen.attrScreenID, sa.screen);
					my_screens.add(sa.screen);
					break;

				case PUMP_SCREEN:
					my_screen_dict.put(sa.screen.attrScreenID, sa.screen);
					my_screens.add(0, sa.screen);
					break;

				case SWAP_SCREENS:
					java.util.Collections.swap(my_screens, sa.index1, sa.index2);
					break;
			}
		}
		my_actions = new ArrayList<ScreenAction>();
	}

	public void rotateUp() {
		my_actions.add(new ScreenAction(Op.ROTATE_UP));
	}

	public void rotateDown() {
		my_actions.add(new ScreenAction(Op.ROTATE_DOWN));
	}

	public void popScreen() {
		my_actions.add(new ScreenAction(Op.POP_SCREEN));
	}

	public void dropScreen() {
		my_actions.add(new ScreenAction(Op.DROP_SCREEN));
	}

	public void pushScreen(Screen s) {
		my_actions.add(new ScreenAction(Op.PUSH_SCREEN, s));
	}

	public void pumpScreen(Screen s) {
		my_actions.add(new ScreenAction(Op.PUMP_SCREEN, s));
	}

	public void removeScreen(Screen s) {
		my_actions.add(new ScreenAction(Op.REMOVE_SCREEN, s));
	}

	public void swapScreens(int index1, int index2) {
		my_actions.add(new ScreenAction(Op.SWAP_SCREENS, index1, index2));
	}

	public Screen getScreenByIndex(int index) {
		return my_screens.get(index);
	}

	public Screen getScreenByID(String id) {
		return my_screen_dict.get(id);
	}

	public java.util.List<Screen> getScreens() {
		return my_screens;
	}

	public java.util.List<Screen> getActiveScreens() {
		java.util.List<Screen> return_value = new ArrayList<Screen>();
		for(Screen s : my_screens) {
			if(s.isActive()) return_value.add(s);
		}
		return return_value;		
	}

	public java.util.List<Screen> getVisibleScreens() {
		java.util.List<Screen> return_value = new ArrayList<Screen>();
		for(Screen s : my_screens) {
			if(s.isVisible()) return_value.add(s);
		}
		return return_value;
	}
}