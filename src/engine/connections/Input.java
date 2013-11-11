package engine.connections;

import java.util.Map;

/**
 * Abstract input class to support Entity I/O
 * 
 * @author dgattey
 * 
 */
abstract public class Input {
	
	/**
	 * Run the input with the given arguments
	 * 
	 * @param args
	 *            the properties to use in running the input
	 */
	public abstract void run(Map<String, String> args);
	
}
