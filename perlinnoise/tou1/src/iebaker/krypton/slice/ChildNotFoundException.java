package iebaker.krypton.slice;


/**
 * Thrown if a search through the layoutnode does not find any children.
 * Side note, wow is it easy to create new exceptions.
 */
public class ChildNotFoundException extends Exception {

	/**
	 * Constructor.  Creates a new ChildNotFoundException with a specified message
	 *
	 * @param message 	An informative error message
	 */
	public ChildNotFoundException(String message) {
		super(message);
	}
}