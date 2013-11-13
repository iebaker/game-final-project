package iebaker.xenon.core;

import java.util.ArrayList;

public class Output {
	private java.util.List<Connection> my_connections = new ArrayList<Connection>();
	
	public void connect(Connection c) { 
		my_connections.add(c);
	}

	public void run() {
		for(Connection c : my_connections) {
			c.run();
		}
	}
}