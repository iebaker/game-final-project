package engine.connections;

import java.io.Serializable;
import java.util.Map;

/**
 * Abstract input class to support Entity I/O
 * 
 * @author dgattey
 * 
 */
abstract public class Input implements Serializable {
	
	private static final long	serialVersionUID	= 2779555456243816855L;
	
	/**
	 * Run the input with the given arguments
	 * 
	 * @param args
	 *            the properties to use in running the input
	 */
	public abstract void run(Map<String, String> args);
	
}
