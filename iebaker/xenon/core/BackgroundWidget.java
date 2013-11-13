package iebaker.xenon.core;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Paint;

/**
 * A utility Widget that just displays a background over a region.  Can be set 
 * to be painted with any awt.Paint object.
 */
public class BackgroundWidget extends Widget {
	private Paint my_bgpaint;

	/**
	 * Constructor.  Don't use this one; I'm keeping it here because I don't want to
	 * go find all the places I used it.
	 *
	 * @param a			The parent Application
	 * @param parent	the parent Screen
	 * @param id		A String to use as the ID for this Widget
	 */
	public BackgroundWidget(Application a, Screen parent, String id) {
		super(a, parent, id);
	}

	/**
	 * Nicer constructor.  Use this one.  This one can learn the parent Application from
	 * the parent Screen.
	 *
	 * @param parent	the parent Screen
	 * @param id		a String to use as the ID for this Widget
	 */
	public BackgroundWidget(Screen parent, String id) {
		super(parent, id);
	}

	/**
	 * Renders the Widget to the screen.
	 */
	@Override
	public void onDraw(Artist a, Graphics2D g) {
		a.setStroke(false);
		a.setFillPaint(my_bgpaint);
		a.rect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y);
	}

	/**
	 * Setter for the paint to be used by this BackgroundWidget.
	 *
	 * @param p		the Paint object to be used to paint the background.
	 */
	public BackgroundWidget setBGPaint(Paint p) {
		my_bgpaint = p;
		return this;
	}
}
