package iebaker.krypton.core;

import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import cs195n.Vec2f;
import java.awt.Paint;

public class Viewport extends Widget {
	
	private Viewable my_viewable;
	private boolean initialized = false;
	private Camera my_camera;
	private Paint my_bg_paint = Color.WHITE;

	public class Camera {
			
		private Vec2f my_focus;
		private float my_zoom;

		public Camera(Vec2f focus, float zoom) {
			my_focus = focus; my_zoom = zoom;
		}

		public void translate(Vec2f trans) {
			my_focus = my_focus.plus(trans);
		}

		public void zoom(float amt) {
			my_zoom *= amt;
		}
	
		public void setFocus(Vec2f newFocus) {
			my_focus = newFocus;
		}

		public void setZoom(float newZoom)  {
			my_zoom = newZoom;
		}

		public Vec2f getFocus() {
			return my_focus;
		}

		public float getZoom() {
			return my_zoom;
		}
	}

	public Viewport(Screen s, String id) {
		super(s, id);
	}

	public void init(Viewable v) {
		my_viewable = v;
		Vec2f focus = my_viewable.getInitialCenter();
		float zoom = my_viewable.getInitialScale();
		my_camera = new Camera(focus, zoom);
		initialized = true;
	}

	@Override
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		float scale = my_camera.getZoom();
		a.setFillPaint(my_bg_paint);
		a.rect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y);
		if(initialized) {
			a.setFillPaint(Color.GRAY);
			a.rect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y);
			AffineTransform preserved = g.getTransform();	
			g.clipRect((int)attrLocation.x, (int)attrLocation.y, (int)attrSize.x, (int)attrSize.y);
		
			Vec2f game_upper_left = my_camera.getFocus().minus(new Vec2f(this.attrSize.x/(2 * scale), this.attrSize.y/(2 * scale)));
			g.translate(-game_upper_left.x * scale, -game_upper_left.y * scale);
			g.scale(scale,scale);

			a.setFillPaint(Color.GREEN);
			my_viewable.render(a, g);
			g.setTransform(preserved);
		} else {
			System.err.println("Attempted to draw an uninitialized viewport.");

			a.setFillPaint(Color.GRAY);
			a.rect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y);
			a.setTextAlign(a.CENTER, a.CENTER);
			a.setFillPaint(Color.BLACK);

			a.text(g, "Viewport has no viewable object to view!", this.getHCenter(), this.getVCenter());
		}
	}

	public Camera getCamera() {
		return my_camera;
	}

	public void setBGPaint(Paint p) {
		my_bg_paint = p;
	}
}
