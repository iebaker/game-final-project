package engine.entity;

import java.util.Map;

import engine.connections.Input;
import engine.connections.Output;

/**
 * Relay Entity that relays a signal out only if enabled
 * 
 * @author dgattey
 * 
 */
public class RelayEntity extends Entity {
	
	private Output	onFire;
	private boolean	enabled;
	
	/**
	 * Constructor, setting default values and making doEnable, doDisable, and doFire new Inputs to use
	 */
	public RelayEntity() {
		super();
		this.mass = 999999999f;
		this.enabled = false;
		onFire = new Output();
		
		/**
		 * Enables the Relay
		 */
		inputs.put("doEnable", new Input() {
			@Override
			public void run(Map<String, String> args) {
				enabled = true;
			}
		});
		
		/**
		 * Disables the Relay
		 */
		inputs.put("doDisable", new Input() {
			@Override
			public void run(Map<String, String> args) {
				enabled = false;
			}
		});
		
		/**
		 * Fires Relay only if already enabled
		 */
		inputs.put("doFire", new Input() {
			@Override
			public void run(Map<String, String> args) {
				if (enabled) fire();
			}
		});
		
		outputs.put("onFire", onFire);
	}
	
	/**
	 * Fires the Relay
	 */
	private void fire() {
		onFire.run();
	}
}
