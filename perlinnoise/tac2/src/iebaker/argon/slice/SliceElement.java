package iebaker.argon.slice;

import cs195n.*;

/**
 * SliceElement is a class which should be extended by any Object you want to function as a 
 * Slice element, i.e. be positionable using Slice.
 */
public class SliceElement {
	public Vec2f attrLocation = new Vec2f(0,0);
	public Vec2f attrSize = new Vec2f(0,0);

	/**
	 * Getter for the location of this element, as a 2-float vector
	 *
	 * @return 	 A Vec2f representation of the location of this element
	 */
	public Vec2f getAttrLocation() {
		return attrLocation;
	}


	/**
	 * Getter for the size of this element, as a 2-float vector
	 *
	 * @return a Vec2f representation of the size of this element
	 */
	public Vec2f getAttrSize() {
		return attrSize;
	}


	/**
	 * Setter for the location of the element
	 *
	 * @param newLocation 		The new location to assign the element
	 */
	public void setAttrLocation(Vec2f newLocation) {
		attrLocation = newLocation;
	}


	/**
	 * Setter for the size of the element
	 *
	 * @param newSize 		The new size to assign the element
	 */
	public void setAttrSize(Vec2f newSize) {
		attrSize = newSize;
	}
}