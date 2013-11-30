package engine;

import game.GameWorld;

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

	private static Vec2f	gameOffset; // in game space

	private static Vec2f	portCoord;	// in screen space

	/**
	 * Calculates the point in game space transformed to screen space
	 * 
	 * @param pointInGame
	 *            A vector to a point in game space
	 * @return The vector in screen space
	 */
	public static Vec2f gamePtToScreen(Vec2f pointInGame) {
		return (((pointInGame.minus(Viewport.getOffset())).smult(Viewport.scale)).plus(Viewport.portCoord));
	}

	/**
	 * Public getter for the current offset
	 * 
	 * @return The current offset
	 */
	public static Vec2f getOffset() {
		return Viewport.gameOffset;
	}

	/**
	 * Calculates the point in screen space transformed to game space
	 * 
	 * @param pointInScreen
	 *            A vector to a point in screen space
	 * @return The vector in game space
	 */
	public static Vec2f screenPtToGame(Vec2f pointInScreen) {
		return ((pointInScreen.minus(Viewport.portCoord)).sdiv(Viewport.scale)).plus(Viewport.getOffset());
	}

	/**
	 * Calculates the point in screen space transformed to game space for offset setting along
	 * 
	 * @param pointInScreen
	 *            A vector to a point in screen space
	 * @return The vector in game space
	 */
	public static Vec2f screenPtToGameForOffset(Vec2f pointInScreen) {
		return (pointInScreen.minus(Viewport.portCoord)).sdiv(Viewport.scale);
	}

	private Vec2f				portEndCoord;	// in screen space
	private final Application	a;
	private static float		scale;
	private static float		zoom;
	private static float		minZoom;

	private static float		maxZoom;

	private Color				c;

	private BasicStroke			stk;

	private boolean				viewChanged;

	public Viewport(Application a) {
		this.a = a;
	}

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
		stk = new BasicStroke(10f);
		this.c = c;
	}

	/**
	 * Public getter for the port ending dim
	 * 
	 * @return The port ending coordinates
	 */
	public Vec2f getDim() {
		return portEndCoord;
	}

	/**
	 * Public getter for the current scale
	 * 
	 * @return The current scale
	 */
	public float getScale() {
		return Viewport.scale;
	}

	/**
	 * Public getter for the port starting dim
	 * 
	 * @return The port starting coordinates
	 */
	public Vec2f getSDim() {
		return Viewport.portCoord;
	}

	/**
	 * Public getter for current zoom level
	 * 
	 * @return
	 */
	public float getZoom() {
		return Viewport.zoom;
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
	 * Draw method, which draws the gameworld and a viewport bounds border
	 * 
	 * @param g
	 */
	public void onDraw(World game, Graphics2D g) {
		GameWorld gameworld = (GameWorld) game;
		double l = (a.getCurrentScreenSize().x + a.getCurrentScreenSize().y);
		double l2 = (game.dim.x + game.dim.y);
		Viewport.scale = (float) (Viewport.zoom * (l / l2));

		float x = Viewport.portCoord.x;
		float y = Viewport.portCoord.y;
		float w = portEndCoord.x - Viewport.portCoord.x;
		float h = portEndCoord.y - Viewport.portCoord.y;
		Rectangle2D bounds = new Rectangle2D.Float(x, y, w, h);

		// Set clip, draw, and unclip
		Rectangle b = g.getClipBounds();
		g.clipRect((int) x, (int) y, (int) w, (int) h);
		game.onDraw(g);
	//	gameworld.getLightingEngineForTesting().rayDebug(gameworld, g);
		gameworld.getLightingEngineForTesting().coneDebug(gameworld, g);
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
		Viewport.portCoord = portCoord;
		this.portEndCoord = portEndCoord;
		viewHasChanged(true);
	}

	public void setGame(World game) {
		game.setPort(this);
		Viewport.gameOffset = new Vec2f(0, 0);
		Viewport.scale = 1f;
		float zmScale = 0.0005f * (game.getDim().mag()); // Makes same scale no matter size of game
		Viewport.zoom = zmScale;
		Viewport.minZoom = 0.2f * zmScale;
		Viewport.maxZoom = 5 * zmScale;

		viewHasChanged(false);
	}

	public void setOffset(Vec2f offset) {
		Viewport.gameOffset = offset;
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

		float newZm = Viewport.zoom * zm;
		if (newZm > Viewport.minZoom && newZm < Viewport.maxZoom) {
			/*
			 * Gets the current pointer location and converts it to screen space. Using that, it changes the gameOffset
			 * and zoom factor
			 */
			Vec2f midPt = (Viewport.screenPtToGame(new Vec2f(p.x, p.y)));
			Viewport.gameOffset = (((Viewport.gameOffset.minus(midPt)).sdiv(zm)).plus(midPt));
			Viewport.zoom = newZm;
			viewHasChanged(true);
		}
	}

}
