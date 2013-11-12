package iebaker.xenon.core;

import cs195n.Vec2f;
import cs195n.Vec2i;

import java.io.File;
import java.io.IOException;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Path2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.Image;

import javax.imageio.ImageIO;


public class Artist {
	public final int TOP = 0;
	public final int LEFT = 1;
	public final int CENTER = 2;
	public final int BOTTOM = 3;
	public final int RIGHT = 4;

	private boolean strokeOn = true;
	private boolean fillOn = true;

	private Paint strokePaint = Color.BLACK;
	private Paint fillPaint = Color.WHITE;

	private int textVertAlign = TOP;
	private int textHorzAlign = LEFT;

	private float fontSize = 10;

	/* --------------------------------- */
	/* UTILITY METHODS                   */
	/* --------------------------------- */	

	/**
	 * Constructs a TexturePaint object from an image located at a specified filename
	 *
	 * @param filename 		The filename of the desired image
	 * @return 				A new TexturePaint object which will paint the desired image
	 */
	public TexturePaint makeTexturePaint(String filename) {
		TexturePaint tp = null;
		try {
			BufferedImage image = ImageIO.read(new File(filename));
			Rectangle2D.Float rect = new Rectangle2D.Float(0,0,image.getWidth(), image.getHeight());
			tp = new TexturePaint(image, rect);
		} catch (IOException e) {
			System.err.println("[Artist.makeTexturePaint()] Failure loading image for texture");
			e.printStackTrace();
		}
		return tp;
	}

	/* --------------------------------- */
	/* SETTING METHODS                   */
	/* --------------------------------- */

	/**
	 * Sets the value of stroke (turns on or off stroked drawing)
	 *
	 * @param s 	A boolean -- true if stroke should be turned on, false otherwise.
	 */ 
	public void setStroke(boolean s) {
		strokeOn = s;
	}

	/**
	 * Sets the value of fill (turns on or off filled drawing)
	 *
	 * @param f 	A boolean -- true if fill should be turned on, false otherwise
	 */
	public void setFill(boolean f) {
		fillOn = f;
	}

	/**
	 * Sets the Paint for stroke drawing
	 *
	 * @param p 	The Paint object to use for stroke drawing
	 */
	public void setStrokePaint(Paint p) {
		strokePaint = p;
	}

	/**
	 * Sets the Paint for filled drawing
	 *
	 * @param p 	The Paint object to use for filled drawing
	 */
	public void setFillPaint(Paint p) {
		fillPaint = p;
	}

	/**
	 * Sets the text alignment to a specific vertical and horizontal value
	 *
	 * @param vert 		The vertical alignment
	 * @param horz 		The horizontal alignment
	 */
	public void setTextAlign(int vert, int horz) {
		textVertAlign = vert;
		textHorzAlign = horz;
	}	

	/**
	 * Sets the font size to a desired floating point value
	 *
	 * @param newFontSize 	The desired new font size.
	 */
	public void setFontSize(float newFontSize) {
		fontSize = newFontSize;
	}

	/* --------------------------------- */
	/* DRAWING METHODS                   */
	/* --------------------------------- */

	/**
	 * General shape drawing method
	 * 
	 * @param g 	the Graphics2D object which will be used to draw
	 * @param s 	the Shape object to draw
	 */
	public void shape(Graphics2D g, Shape s) {
		if(fillOn) {
			g.setPaint(fillPaint);
			g.fill(s);
		}
		if(strokeOn) {
			g.setPaint(strokePaint);
			g.draw(s);
		}	
	}

	/**
	 * Draws a rectangle
	 *
	 * @param g 		The Graphics2D object which will be used to draw
	 * @param x 		The x position of the rectangle
	 * @param y 		The y position of the rectangle
	 * @param width 	The width of the rectangle
	 * @param height 	The height of the rectangle
	 */
	public void rect(Graphics2D g, float x, float y, float width, float height) {
		shape(g, new Rectangle2D.Float(x, y, width, height));
	}

	/**
	 * Draws a path 
	 *
	 * @param g 		The Graphics2D object which will be used for drawing
	 * @param points	The list of poings describing the path (in counterclockwise order around the shape)
	 */
	public void path(Graphics2D g, java.util.List<Vec2f> points) {
		Path2D.Float path = new Path2D.Float();
		for(Vec2f point : points) {
			if(points.indexOf(point) == 0)
			path.moveTo(point.x, point.y);
			else
			path.lineTo(point.x, point.y);
		}
		path.closePath();
		this.shape(g, path);
	}

	/**
	 * Draws a rectangle with rounded borders
	 *
	 * @param g 		The graphics2D object which will be used to draw
	 * @param x 		The x position of the rectangle
	 * @param y 		The y position of the rectangle
	 * @param width 	The width of the rectangle
	 * @param height 	The height of the rectangle
	 * @param arcwidth 	The arc width of the corners of the rectangle
	 * @param archeight The arc height of the corners of the rectangle
	 */
	public void roundrect(Graphics2D g, float x, float y, float width, float height, float arcwidth, float archeight) {
		shape(g, new RoundRectangle2D.Float(x, y, width, height, arcwidth, archeight));
	}

	/**
	 * Draws an ellipse
	 *
	 * @param g 		The graphics2D object which will be used to draw
	 * @param x 		The x position of the bounding rectangle of the ellipse
	 * @param y 		The y position of the bounding rectangle of the ellipse
	 * @param width 	The width of the ellipse
	 * @param height 	The height of the ellipse
	 */
	public void ellipse(Graphics2D g, float x, float y, float width, float height) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		shape(g, new Ellipse2D.Float(x, y, width, height));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	/**
	 * Draws text
	 *
	 * @param g 		The graphics2D object which will be used to draw
	 * @param s 		The string to draw
	 * @param x 		The x position of the text
	 * @param y 		The y position of the text
	 */
	public void text(Graphics2D g, String s, float x, float y) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(fillOn) {
			g.setPaint(fillPaint);
			g.setFont(g.getFont().deriveFont(fontSize));
			int	width = g.getFontMetrics().stringWidth(s);
			int height = g.getFontMetrics().getHeight();

			switch(textVertAlign) {
				case CENTER:
					y = y - (float)height/2 + g.getFontMetrics().getAscent();
					break;

				case BOTTOM:
					y = y - (float)height;
					break;
			}

			switch(textHorzAlign) {
				case CENTER:
					x = x - (float)width/2;
					break;

				case RIGHT:
					x = x - (float)width;
					break;
			}

			g.drawString(s, x, y);
		}
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	/**
	 * Renders an image.  The image can be scaled optionally.
	 *
	 * @param g 		the Graphics2D object used to draw
	 * @param img		the Image to draw
	 * @param x			the integer x position at which to draw the image.
	 * @param y 		the integer y position at which to draw the image
	 * @param width		the width of the image to draw	(will scale if not the same aspect ratio)
	 * @param height	the height of the image to draw (will scale if not the same aspect ratio)
	 */
	public void image(Graphics2D g, Image img, int x, int y, int width, int height) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(img, x, y, width, height, null);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}
