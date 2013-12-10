package engine.ui;

import java.awt.Graphics2D;
import java.util.Map;

import engine.World;
import engine.connections.Input;
import engine.entity.Entity;

/**
 * Class representing a text box that appears during cutscenes
 * @author Sawyer
 *
 */
public class TextBox extends Entity {

	private static final long serialVersionUID = 8391136443225937238L;
	//private boolean active = false;
	private UIRoundRect rect;
	private UIText text;
	private Map<String, String> currArgs;
	private int argCount = 1;
	private boolean visible = false;

	/**
	 * Constructor. Creates the Input that allows the TextBox to be used
	 */
	public TextBox(UIRoundRect rect, UIText textBox) {
		this.rect = rect;
		this.text = textBox;
		
		this.inputs.put("displayText", new Input() {

			private static final long serialVersionUID = 5782831638451389990L;

			@Override
			public void run(Map<String, String> args) {
				TextBox.this.currArgs = args;
				TextBox.this.setVisible(true);
				text.updateText(args.get("text1"));
				argCount = 2;
				TextBox.super.world.enterCutscene();
			}
		});
	}
	
	/**
	 * 
	 * @return the rectangle of the box
	 */
	public UIRoundRect getRect() {
		return rect;
	}
	
	/**
	 * 
	 * @return the text of the box
	 */
	public UIText getText() {
		return text;
	}
	
	/**
	 * Sets the rectangle to be the given one
	 * @param rect
	 */
	public void resetRect(UIRoundRect rect) {
		this.rect = rect;
	}
	
	/**
	 * Sets the text to be the given text
	 * @param text
	 */
	public void resetText(UIText text) {
		this.text = text;
	}
	
	/**
	 * Sets the world
	 * @param w
	 */
	public void setWorld(World w) {
		this.world = w;
	}
	
	/**
	 * 
	 * @return true if there is another line of text to be displayed, false if not
	 */
	public boolean hasNextLine() {
		return currArgs.containsKey("text" + argCount);
	}
	
	/**
	 * displays the next line of text
	 */
	public void displayNext() {
		text.updateText(currArgs.get("text" + argCount));
		argCount++;
	}
	
	/**
	 * controls the visibility of the text and rectangle
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		text.setVisible(visible);
		rect.setVisible(visible);
	}
	
	/**
	 * 
	 * @return visible
	 */
	public boolean getVisible() {
		return visible;
	}
	
	/**
	 * Draws the textbox
	 * @param g
	 */
	public void draw(Graphics2D g) {
		rect.drawAndFillShape(g);
		text.drawShape(g);
	}
	
	public void displayText(Map<String, String> args) {
		this.inputs.get("displayText").run(args);
	}
}
