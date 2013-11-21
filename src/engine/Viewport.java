package engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2f;

/**
 * Viewport class
 * 
 * @author dgattey
 * 
 */
public class Viewport {
	
	private Vec2f		gameOffset;	// in game space
	private Vec2f		portCoord;		// in screen space
	private Vec2f		portEndCoord;	// in screen space
	private Application	a;
	private float		scale;
	private float		zoom;
	private float		minZoom;
	private float		maxZoom;
	private Color		c;
	private BasicStroke	stk;
	private boolean		viewChanged;
	
	/**
	 * Constructor for Viewport
	 * 
	 * @param a
	 *            Application
	 * @param c
	 *            Color for the border
	 * @param game
	 *            The game world to show
	 * @param zoom
	 *            Initial zoom
	 * @param minZoom
	 *            Minimum zoom
	 * @param maxZoom
	 *            Maximum zoom
	 */
	public Viewport(Application a, Color c, World game) {
		this(a);
		this.stk = new BasicStroke(10f);
		this.c = c;
	}
	
	public Viewport(Application a) {
		this.a = a;
	}
	
	public void setGame(World game) {
		game.setPort(this);
		this.gameOffset = new Vec2f(0, 0);
		this.scale = 1f;
		float zmScale = 0.0005f * (game.getDim().mag()); // Makes same scale no matter size of game
		this.zoom = zmScale;
		this.minZoom = 0.8f * zmScale;
		this.maxZoom = 2 * zmScale;
		
		this.viewHasChanged(false);
	}
	
	/**
	 * Draw method, which draws the gameworld and a viewport bounds border
	 * 
	 * @param g
	 */
	public void onDraw(World game, Graphics2D g) {
		double l = (a.getCurrentScreenSize().x + a.getCurrentScreenSize().y);
		double l2 = (game.dim.x + game.dim.y);
		this.scale = (float) (zoom * (l / l2));
		
		float x = portCoord.x;
		float y = portCoord.y;
		float w = portEndCoord.x - portCoord.x;
		float h = portEndCoord.y - portCoord.y;
		Rectangle2D bounds = new Rectangle2D.Float(x, y, w, h);
		
		// Set clip, draw, and unclip
		Rectangle b = g.getClipBounds();
		g.clipRect((int) x, (int) y, (int) w, (int) h);
		game.onDraw(g);
		g.clip(b);
		
		// Draw a box to show the viewport
		g.setColor(c);
		if (stk != null) {
			g.setStroke(stk);
			g.draw(bounds);
		}
	}
	
	/**
	 * Changes port coord and boolean for resize view
	 * 
	 * @param portCoord
	 * @param portEndCoord
	 */
	public void resizeView(Vec2f portCoord, Vec2f portEndCoord) {
		this.portCoord = portCoord;
		this.portEndCoord = portEndCoord;
		this.viewHasChanged(true);
	}
	
	/**
	 * Zooms the view in or out but only if in bounds of port
	 * 
	 * @param zoom
	 *            The zoom scalar from the mouse wheel
	 */
	public void zoomView(Vec2f p, float zm) {
		if (zm < 0)
			zm = 0.95f;
		else
			zm = 1.05f;
		
		float newZm = this.zoom * zm;
		if (newZm > minZoom && newZm < maxZoom) {
			/*
			 * Gets the current pointer location and converts it to screen space. Using that, it changes the gameOffset
			 * and zoom factor
			 */
			Vec2f midPt = (screenPtToGame(new Vec2f(p.x, p.y)));
			this.gameOffset = (((this.gameOffset.minus(midPt)).sdiv(zm)).plus(midPt));
			this.zoom = newZm;
			viewHasChanged(true);
		}
	}
	
	/**
	 * Calculates the point in game space transformed to screen space
	 * 
	 * @param pointInGame
	 *            A vector to a point in game space
	 * @return The vector in screen space
	 */
	public Vec2f gamePtToScreen(Vec2f pointInGame) {
		return (((pointInGame.minus(getOffset())).smult(scale)).plus(portCoord));
	}
	
	/**
	 * Calculates the point in screen space transformed to game space
	 * 
	 * @param pointInScreen
	 *            A vector to a point in screen space
	 * @return The vector in game space
	 */
	public Vec2f screenPtToGame(Vec2f pointInScreen) {
		return ((pointInScreen.minus(portCoord)).sdiv(scale)).plus(getOffset());
	}
	
	/**
	 * Calculates the point in screen space transformed to game space for offset setting along
	 * 
	 * @param pointInScreen
	 *            A vector to a point in screen space
	 * @return The vector in game space
	 */
	public Vec2f screenPtToGameForOffset(Vec2f pointInScreen) {
		return (pointInScreen.minus(portCoord)).sdiv(scale);
	}
	
	/**
	 * Public getter for the current offset
	 * 
	 * @return The current offset
	 */
	public Vec2f getOffset() {
		return gameOffset;
	}
	
	public void setOffset(Vec2f offset) {
		this.gameOffset = offset;
	}
	
	/**
	 * Public getter for the current scale
	 * 
	 * @return The current scale
	 */
	public float getScale() {
		return scale;
	}
	
	/**
	 * Public getter for the port starting dim
	 * 
	 * @return The port starting coordinates
	 */
	public Vec2f getSDim() {
		return this.portCoord;
	}
	
	/**
	 * Public getter for the port ending dim
	 * 
	 * @return The port ending coordinates
	 */
	public Vec2f getDim() {
		return this.portEndCoord;
	}
	
	/**
	 * Public getter for viewChanged, used to coordinate drawing in the game from the viewport
	 * 
	 * @return viewChanged
	 */
	public boolean hasViewChanged() {
		return viewChanged;
	}
	
	/**
	 * Public setter for viewChanged, used to coordinate drawing in the game from the game
	 * 
	 * @param viewChanged
	 *            The new viewChanged
	 */
	public void viewHasChanged(boolean viewChanged) {
		this.viewChanged = viewChanged;
	}
	
	/**
	 * Public getter for current zoom level
	 * 
	 * @return
	 */
	public float getZoom() {
		return zoom;
	}
	
}
