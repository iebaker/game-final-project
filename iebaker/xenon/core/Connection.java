package iebaker.xenon.core;

import java.util.HashMap;

/**
 * Connection is a class which represents a link between Entities.  A Connection is
 * specifically tied to a single Input.
 */
public class Connection {

	private Input target;

	/**
	 * Constructor. 
	 *
	 * @param i 	The Input object which this connection provides a link to.
	 */
	public Connection(Input i) {
		target = i;
	}
	
	private java.util.Map<String, String> args = new HashMap<String, String>();

	/**
	 * Calls the target input and runs it.
	 */
	public void run() {
		target.run(args);
	}
}
