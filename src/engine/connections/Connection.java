package engine.connections;

import java.io.Serializable;
import java.util.Map;

/**
 * Public class allowing connections to be made for an Input with args
 * 
 * @author dgattey
 * 
 */
public class Connection implements Serializable {
	
	private static final long			serialVersionUID	= 5396046908985397086L;
	private final Input					target;
	private final Map<String, String>	args;
	
	/**
	 * Constructor sets target and args
	 * 
	 * @param i
	 *            the input to run on this connection
	 * @param args
	 *            the arguments passed to the target
	 */
	public Connection(Input i, Map<String, String> args) {
		this.target = i;
		this.args = args;
	}
	
	/**
	 * Run the input with the given arguments
	 */
	public void run() {
		target.run(args);
	}
	
}
