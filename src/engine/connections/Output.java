package engine.connections;

import java.util.ArrayList;
import java.util.List;

/**
 * Output class to use for Entity I/O
 * 
 * @author dgattey
 * 
 */
public class Output {
	
	private List<Connection>	connections;
	
	/**
	 * Creates a connection between this output and the input connection
	 * 
	 * @param c
	 *            a Connection to add to this connections list
	 */
	public void connect(Connection c) {
		connections = new ArrayList<Connection>();
		connections.add(c);
	}
	
	/**
	 * Run each connection when run is called for this output
	 */
	public void run() {
		for (Connection c : connections)
			c.run();
	}
	
}
