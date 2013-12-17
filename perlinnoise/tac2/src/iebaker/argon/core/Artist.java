package iebaker.argon.core;

import java.awt.*;
import java.awt.geom.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;

/**
 * Artist is a class which encapsulates Graphics2D drawing calls in an easier to use API
 */
public class Artist {
	
	/**
	 * Represents text aligning to the TOP
	 */
	public final int TOP = 0;

	/**
	 * Represents text aligning to the LEFT
	 */
	public final int LEFT = 1;

	/**
	 * Represents text aligning to the CENTER
	 */
	public final int CENTER = 2;

	/**
	 * Represents text aligning to the BOTTOM
	 */ 
	public final int BOTTOM = 3;

	/**
	 * Represents text aligning to the RIGHT
	 */
	public final int RIGHT = 4;

	/**
	 * Whether to draw stroked outlines of Shapes
	 */
	private boolean strokeOn = true;

	/**
	 * Whether to fill the inside of drawn Shapes
	 */
	private boolean fillOn = true;

	/**
	 * Paint which is used to stroke the outlines of shapes, if strokeOn is true
	 */
	private Paint strokePaint = Color.BLACK;

	/**
	 * Paint which is used to fill the inside of shapes, if fillOn is true
	 */
	private Paint fillPaint = Color.WHITE;

	/**
	 * The vertical alignment of text
	 */
	private int textVertAlign = TOP;

	/**
	 * The horizontal alignment of text
	 */
	private int textHorzAlign = LEFT;

	/**
	 * The size of the rendered font
	 */
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
		TexturePaint new_tp = null;
		try {
			File image_file = new File(filename);
			BufferedImage image = ImageIO.read(image_file);
			Rectangle2D.Float rect = new Rectangle2D.Float(0,0,image.getWidth(), image.getHeight());
			new_tp = new TexturePaint(image, rect);
		} catch (IOException e) {
			System.err.println("Failure loading image for texture");
		}
		return new_tp;
	}

	/* --------------------------------- */
	/* SETTING METHODS                   */
	/* --------------------------------- */


	/**
	 * Turns on stroked drawing
	 */
	public void strokeOn() {
		strokeOn = true;
	}


	/**
	 * Turns off stroked drawing
	 */
	public void strokeOff() {
		strokeOn = false;
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
	 * Turns on filled drawing
	 */
	public void fillOn() {
		fillOn = true;
	}


	/**
	 * Turns off filled drawing
	 */
	public void fillOff() {
		fillOn = false;
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
}