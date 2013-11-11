package engine.collision;

/**
 * Class for finding range, expanding range, and checking two ranges for use with projections
 * 
 * @author dgattey
 * 
 */
public class Range {
	
	private float	min;
	private float	max;
	
	/**
	 * Public constructor
	 * 
	 * @param min
	 *            The minimum point
	 * @param max
	 *            The maximum point
	 */
	public Range(float min, float max) {
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Getter for min
	 * 
	 * @return The min of this range
	 */
	protected float getMin() {
		return min;
	}
	
	/**
	 * Getter for max
	 * 
	 * @return The max of this range
	 */
	protected float getMax() {
		return max;
	}
	
	/**
	 * Expands the range if the given vector has a bigger range in any direction
	 * 
	 * @param r
	 *            The range to expand with
	 */
	protected void expandRange(Range r) {
		if (r.min < this.min) this.min = r.min;
		if (r.max > this.max) this.max = r.max;
	}
	
	/**
	 * Checks if two ranges overlap
	 * 
	 * @param b
	 *            the range to check against for overlap
	 * @return If this overlaps with b
	 */
	protected boolean overlapping(Range b) {
		return (min < b.getMax() && b.getMin() < max);
	}
	
	/**
	 * String representation of Range
	 */
	public String toString() {
		return "Range<" + min + " to " + max + ">";
	}
	
}
