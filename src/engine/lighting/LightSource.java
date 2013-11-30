package engine.lighting;

import java.awt.Color;
import java.util.List;

import cs195n.Vec2f;

/**
 * LightSource is a class which represents a single light in the world. A LightSource owns its location, color, and
 * brightness (between 0 and 1). In addition, it owns a List of LightCones which represent the areas which are
 * illuminated by this light.
 */
public class LightSource {
	
	private Vec2f			my_location;
	private Color			my_color		= new Color(255, 255, 153); // FFFF99
	private float			my_brightness	= 0.5f;
	private List<LightCone>	my_light_cones;
	
	/**
	 * Constructor. Just sets the light's location -- color defaults to FFFF99 (light yellow) and brightness defaults to
	 * 0.5
	 */
	public LightSource(Vec2f loc) {
		my_location = loc;
	}
	
	/**
	 * Accessor for the brightness of the light
	 * 
	 * @return a float between 0 and 1 representing the brightness of the light
	 */
	public float getBrightness() {
		return my_brightness;
	}
	
	/**
	 * Accessor for the color of the light
	 * 
	 * @return The Color of the light
	 */
	public Color getColor() {
		return my_color;
	}
	
	/**
	 * Accessor for the location of the light
	 * 
	 * @return a Vec2f representation of the location of the light
	 */
	public Vec2f getLocation() {
		return my_location;
	}
	
	/**
	 * Setter for the brightness field
	 * 
	 * @param f
	 *            The new brightness of the light; must be between 0 and 1
	 */
	public void setBrightness(float f) {
		if (f >= 0 && f <= 1) {
			my_brightness = f;
		}
	}
	
	/**
	 * Setter for the color field
	 * 
	 * @param c
	 *            The new color of the light
	 */
	public void setColor(Color c) {
		my_color = c;
	}
	
	/**
	 * Setters for the light cones owned by this LightSource object
	 * 
	 * @param cones
	 *            The list of LightCone objects to be associated with this light
	 */
	public void setLightCones(List<LightCone> cones) {
		my_light_cones = cones;
	}

	public List<LightCone> getLightCones() {
		return my_light_cones;
	}
	
	/**
	 * Setter for the location field
	 * 
	 * @param v
	 *            The new location of the light
	 */
	public void setLocation(Vec2f v) {
		my_location = v;
	}
	
	/**
	 * Returns a useful string representation of this lightsource object
	 */
	@Override
	public String toString() {
		return "[engine.lighting.LightSource LOCATION=" + my_location + "]";
	}
}