package iebaker.krypton.core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ScreenManager is a class which exposes functionality
 * for manipulating the stack (list) of screens owned by the
 * parent application.  The ScreenManager has a reference to the
 * parent application, as well as reference to the parent's list
 * of screens.  The ScreenManager maintains a dictionary which
 * indexes all currently owned screens by their ScreenID attribute
 * so that a single screen can be retrieved if desired. Pushing/
 * Pumping a screen adds it to the dictionary, Popping/Dropping a
 * screen removes it from the dictionary.
 */
public class ScreenManager {
	private java.util.List<Screen> screens;
	private java.util.Map<String, Screen> screen_dict;
	private Application parent;


	/**
	 * Constructor.  Takes a reference to the parent application, as 
	 * well as a reference to the parent applications's list of screens.
	 *
	 * @param a 	a reference to the parent application
	 * @param s 	the parent application's list of screens
	 */
	public ScreenManager(Application a, java.util.List<Screen> s) {
		parent = a;
		screens = s;
		screen_dict = new HashMap<String, Screen>();
	}


	/**
	 * Increments the indices of the screens in the list 
	 * (modulo the length of the list).
	 */
	public void rotateUp() {
		screens.add(0, screens.remove(screens.size() - 1));
	}

	public void clearScreens() {
		screens = new ArrayList<Screen>();
		screen_dict = new HashMap<String, Screen>();
	}


	/**
	 * Decrements the indices of the screens in the list
	 * (modulo the length of the list).
	 */
	public void rotateDown() {
		screens.add(screens.size() - 1, screens.remove(0));
	}


	/**
	 * Removes the screen at the top of the list as well
	 * as clearing the entry for that screen in the
	 * dictionary.
	 *
	 * @return 		the screen removed
	 */
	public Screen popScreen() {
		Screen popped = screens.remove(screens.size() - 1);
		screen_dict.remove(popped.attrScreenID);
		return popped;
	}


	/**
	 * Removes the screen at the bottom of the stack, as
	 * well as clearing the entry for that screen in the
	 * dictionary
	 *
	 * @return 		the screen removed
	 */
	public Screen dropScreen() {
		Screen dropped = screens.remove(0);
		screen_dict.remove(dropped.attrScreenID);
		return dropped;
	}


	/**
	 * Removes a specific screen from the stack, as well
	 * as clearing that screen's entry from the dictionary.
	 *
	 * @param s 	the screen to be removed
	 */
	public void removeScreen(Screen s) {
		screen_dict.remove(s.attrScreenID);
		screens.remove(s);
	}
	

	/**
	 * Adds a screen to the top of the stack and indexes
	 * that screen in the dictionary by its ScreenID
	 *
	 * @param s 	the Screen to be added
	 */
	public void pushScreen(Screen s) {
		screen_dict.put(s.attrScreenID, s);
		screens.add(s);
	}


	/**
	 * Adds a screen to the bottom of the stack and indexes 
	 * that screen in the dictionary by its ScreenID
	 *
	 * @param s 	the Screen to be added
	 */
	public void pumpScreen(Screen s) {
		screen_dict.put(s.attrScreenID, s);
		screens.add(0, s);
	}


	/**
	 * Swaps the indices of two screens in the stack
	 *
	 * @param index1 	The index of one screen
	 * @param index2 	The index of another screen
	 */
	public void swapScreens(int index1, int index2) {
		java.util.Collections.swap(screens, index1, index2);
	}


	/**
	 * Retrieval method for a screen indexed at a
	 * specific position in the stack.
	 *
	 * @param index 	the index of the screen to be retrieved
	 * @return 			the Screen at that index
	 */
	public Screen getScreenByIndex(int index) {
		return screens.get(index);
	}


	/**
	 * Retrieval method for a screen indexed by a 
	 * specific ScreenID in the dictionary
	 *
	 * @param id 	the ScreenID of the screen to be retrieved
	 * @return 		the Screen with that ScreenID
	 */
	public Screen getScreenByID(String id) {
		return screen_dict.get(id);
	}


	/**
	 * Retrieval method for all screens, because the parent application
	 * does not expose them.
	 *
	 * @return 		The list of screens
	 */
	public java.util.List<Screen> getScreens() {
		return screens;
	}


	/**
	 * Retrieval method for just active screens.
	 *
	 * @return 		a list containing all sctive screens
	 */
	public java.util.List<Screen> getActiveScreens() {
		java.util.List<Screen> return_value = new ArrayList<Screen>();
		for(Screen s : screens) {
			if(s.isActive()) return_value.add(s);
		}
		return return_value;
	}


	/**
	 * Retrieval method for just visible screens.
	 *
	 * @return 		a list containing all visible screens
	 */
	public java.util.List<Screen> getVisibleScreens() {
		java.util.List<Screen> return_value = new ArrayList<Screen>();
		for(Screen s : screens) {
			if(s.isVisible()) return_value.add(s);
		}
		return return_value;
	}
}	

