package iebaker.xenon.core;

import cs195n.Vec2f;
import java.util.HashMap;
import iebaker.xenon.geom.Shape;
import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;

/**
 * Entity is a high level class which represents an element of the game world.  Entities
 * have many properties, including their position, velocity, mass, coefficient of restitution,
 * IO connections, a name, a shape, etc... 
 */
public class Entity {
	protected Vec2f 						my_position = new Vec2f(0,0);
	protected Vec2f 						my_velocity = new Vec2f(0,0);
	protected Vec2f 						total_force = new Vec2f(0,0);
	protected Vec2f 						total_impulse = new Vec2f(0,0);
	protected World 						my_world = null;
	protected float 						restitution = 0.01f;
	protected float 						my_mass = 1f;
	protected java.util.Map<String, String> my_stats = new HashMap<String, String>();
	protected boolean 						fixed = false;
	protected float 						maxVelocity = 100;
	protected boolean 						jumpable = false;
	protected Shape 						my_shape = new iebaker.xenon.geom.AAB(new Vec2f(0,0), new Vec2f(0,0));
	protected String 						my_name = "";
	protected java.util.Map<String, Input>  my_inputs = new HashMap<String, Input>();
	protected java.util.Map<String, Output> my_outputs = new HashMap<String, Output>();
	protected boolean						intangible = false;

	/**
	 * Constructor.  
	 *
	 * @param pos	The initial position of the Entity
	 * @param vel	The intial velocity of the Entity
	 * @param m		The mass of the Entity
	 * @param name	a String by which to identify the Entity
	 */
	public Entity(Vec2f pos, Vec2f vel, float m, String name) {
		my_position = pos;
		my_velocity = vel;
		my_mass = m;
		my_world = null;
		my_name = name;
	}

	/**
	 * Accessor method for an Input.  
	 *
	 * @param s 	The name by which the Input was indexed when it was added 
	 * @return		The input desired
	 */
	public Input getInput(String s) {
		return my_inputs.get(s);
	}

	/**
	 *	Accessor method for an Output.
	 *
	 * @param s		The name by which the Output was indexed when it was added
	 * @return		The output desired
	 */
	public Output getOutput(String s) {
		return my_outputs.get(s);
	}

	/**
	 * Method to add outputs to an Entity.
	 *
	 * @param s		A list of Strings representing the names by which to index the Outputs
	 */
	protected void addOutputs(String... s) {
		for(String str : s) {
			my_outputs.put(str, new Output());
		}
	}

	/**
	 * Method to add inputs to an Entity.
	 *
	 * @param s		The name by which to index this Input
	 * @param i		The Input object to add
	 */
	protected void addInput(String s, Input i) {
		my_inputs.put(s, i);
	}

	/**
	 * Setter method for the Shape of an entity.  An entity's Shape is used for collision
	 * calculation (and usually rendering).
	 *
	 * @param s		the Shape to use
	 */
	public void setShape(Shape s) {
		my_shape = s;
	}


	/**
	 * Accessor method for tangibility field.  I wouldn't recommend using this and, instead
	 * I'd probably write something like this into a game itself and not the engine... Eh :/
	 *
	 * @return 		true, if the Entity is tangible, false otherwise.
	 */
	public boolean isTangible() {
		return !intangible;
	}


	/**
	 * Draws the entity to the screen.  By default this is done by asking the Shape object
	 * to use its own drawSelf method.  Many games will desire to override this.
	 *
	 * @param a		an Artist object used to format the Graphics
	 * @param g		the Graphics2D object used to draw
	 */
	public void render(Artist a, Graphics2D g) {
		if(my_shape != null) my_shape.at(my_position).drawSelf(a, g);
	}


	/**
	 * Translates the Entity out of collision.  Done by actually mutating the position field.
	 *
	 * @param mtv	The vector by which to translate the position.
	 */
	public void unCollide(Vec2f mtv) {
		if(!fixed) {
			my_position = my_position.plus(mtv);
			if(mtv.x == 0f && mtv.y < 0) {
				jumpable = true;
			}
		}
	}


	/**
	 * Logic for Entities which must interract on collision should go here.  Usually this will
	 * consist of firing outputs and not directly modifying the other Entity.
	 *
	 * @param e		The entity we just collided with.
	 */
	public void onCollideWith(Entity e) {
		return;
	}


	/**
	 * Accessor method for the Shape assigned to this Entity
	 *
	 * @return 		the Shape object associated with this Entity and used to collide it.
	 */
	public Shape getShape() {
		if(my_shape != null) return my_shape.at(my_position);
		return null;
	}


	/**
	 * Sets this Entity's position to be static.
	 */
	public void fix() {
		fixed = true;
	}


	/**
	 * Sets this Entity's position to be non-static.
	 */
	public void unfix() {
		fixed = false;
	}


	/**
	 * Accessor method for the Entity's static-ness.
	 *
	 * @return 		true if this is a fixed (static) Entity, false otherwise.
	 */
	public boolean isFixed() {
		return fixed;
	}


	/**
	 * Accessor method for the coefficent of restitution of this Entity.
	 * 
	 * @return 		a floating point representation of the COR.
	 */
	public float getCOR() {
		return restitution;
	}


	/**
	 * Logic for the Entity mutating or passing messages to the World during a tick
	 * should go here.  Usually overriden by game code.
	 */
	public void affectWorld() {
		return;
	}


	/**
	 * Applies a force to the Entity.
	 *
	 * @param force		a vector representing the force to be applied
	 */
	public void addForce(Vec2f force) {
		if(!Float.isNaN(force.x) && !Float.isNaN(force.y))
		total_force = total_force.plus(force);
	}


	/**
	 * Applies an impulse to the Entity.
	 *
	 * @param impulse	a vector representing the impulse to be applied
	 */
	public void addImpulse(Vec2f impulse) {
		if(!Float.isNaN(impulse.x) && !Float.isNaN(impulse.y))
		total_impulse = total_impulse.plus(impulse);
	}


	/**
	 * Performs Symplectic Eulerian integration in order to update the position and velocity of the Entity
	 * based on Force and Impulse applied over the last tick.
	 *
	 * @param dt 	The time (in seconds) which has elapsed over this tick.
	 */
	public void update(float dt) {
		jumpable = false;
		if(!fixed && !Float.isNaN(total_force.x) && !Float.isNaN(total_force.y) && !Float.isNaN(total_impulse.x) && !Float.isNaN(total_impulse.y)) {
			my_velocity = my_velocity.plus(total_force.smult(dt/my_mass)).plus(total_impulse.smult(1.0f/(float)my_mass));
			my_position = my_position.plus(my_velocity.smult(dt));
		}
		total_force = new Vec2f(0,0);
		total_impulse = new Vec2f(0,0);
	}


	/**
	 * Adds the Entity to a World.
	 *
	 * @param w 	The world to add the Entity to.
	 */
	public void bindToWorld(World w) {
		my_world = w;
		my_world.addEntity(this);
	}


	/**
	 * Removes the Entity from its world.
	 */
	public void freeFromWorld() {
		//System.out.println("World:" + my_world);
		my_world.removeEntity(this);
		my_world = null;
	}


	/**
	 * Accessor method for the position of this Entity.
	 *
	 * @return  	a Vec2f representation of the Entity's position.
	 */
	public Vec2f getPosition() {
		return my_position;
	}


	/**
	 * Accessor method for the velocity of the Entity.
	 *
	 * @return 		a Vec2f representation of the Entity's velocity.
	 */
	public Vec2f getVelocity() {
		return my_velocity;
	}


	/**
	 * Accessor method for the mass of the Entity.
	 *
	 * @return 		a floating point representation of the Entity's mass.
	 */
	public float getMass() {
		return my_mass;
	}


	/**
	 * Returns the name used to index this Entity in the world.
	 *
	 * @return 		a String representing the name of the entity.
	 */
	public String getName() {
		return my_name;
	}
}
