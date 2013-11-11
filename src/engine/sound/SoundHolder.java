package engine.sound;

import java.util.Hashtable;

/**
 * A setup to make sound management a little bit easier.
 * Should be instantiated when the game starts, and all of the sound files should be added to the hashtable
 * 
 * @author smt3
 *
 */
public class SoundHolder {

	public static final Hashtable<String, Sound> sounds = new Hashtable<String, Sound>();
	
	/**
	 * Creates a new sound from the given path and adds it to the Hashtable with the given name as a key
	 * 
	 * @param name
	 * @param path
	 */
	public void addSound(String name, String path) {
		sounds.put(name, new Sound(path));
	}
}
