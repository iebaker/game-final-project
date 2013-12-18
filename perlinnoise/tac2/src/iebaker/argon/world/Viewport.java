package iebaker.argon.world;

import cs195n.*;
import iebaker.argon.core.Widget;
import iebaker.argon.core.Application;
import iebaker.argon.core.Screen;
import iebaker.argon.world.Universe;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.Graphics2D;

public class Viewport extends Widget {

	private Universe rendered_universe;

	protected Vec2f curr_mouse_position = new Vec2f(0, 0);
	protected Vec2f prev_mouse_position = new Vec2f(0, 0);

	private float scale;
	private float max_scale = 0.5f;
	private float min_scale = 0.01f;

	private float friction = -0.1f;

	private Vec2f center_position;
	private Vec2f velocity = new Vec2f(0, 0);

	private float min_velocity = 1;
	private float max_velocity = 300;

	private boolean dragging;

	public Viewport(Application a, Screen s, String id, Universe u) {
		super(a, s, id);
		rendered_universe = u;
		center_position = rendered_universe.getStartingCenter();
		scale = rendered_universe.getStartingScale();
	}

	@Override
	public void onTick(long nanos) {
		rendered_universe.onTick(nanos);

		Vec2f mouse_vector = prev_mouse_position.minus(curr_mouse_position);

		if(velocity.mag() < min_velocity) {
			velocity = new Vec2f(0, 0);
		} else if (velocity.mag() > max_velocity) {
			Vec2f normalized = new Vec2f(0, 0);
			if(!velocity.isZero()) normalized = velocity.normalized();
			velocity = normalized.smult(max_velocity);
		}

		//If the mouse is being dragged, move the view around
		if(dragging) {
			float dist = mouse_vector.mag()/scale;
			Vec2f normalized = new Vec2f(0, 0);
			if(!mouse_vector.isZero()) normalized = mouse_vector.normalized();
			Vec2f mover_vector = normalized.smult(dist);
			center_position = center_position.plus(mover_vector);
			if(!mover_vector.isZero()) velocity = mover_vector;

		} else {
			velocity = velocity.plus(new Vec2f(velocity.x * friction, velocity.y * friction));
			center_position = center_position.plus(velocity);
		}

		prev_mouse_position = curr_mouse_position;

	}

	@Override
	public void onDraw(Graphics2D g) {
		AffineTransform preserved = g.getTransform();
		g.clipRect((int)this.attrLocation.x, (int)this.attrLocation.y, (int)this.attrSize.x, (int)this.attrSize.y);
		Vec2f game_upper_left = center_position.minus(new Vec2f(this.attrSize.x/(2 * scale), this.attrSize.y/(2 * scale)));
		g.translate(-game_upper_left.x * scale, -game_upper_left.y * scale);

		g.scale(scale, scale);

		a.setFillPaint(new Color(200, 195, 175));
		//a.rect(g, 2.5f * rendered_universe.getGridwidth(), 2.5f * rendered_universe.getGridwidth(),
		//	rendered_universe.getTotalWidth() + 20, rendered_universe.getTotalHeight() + 20);
		rendered_universe.onDraw(a, g);
		g.setTransform(preserved);
	}

	public void setToWorldCoords(Graphics2D g) {
		Vec2f game_upper_left = center_position.minus(new Vec2f(this.attrSize.x/(2 * scale), this.attrSize.y/(2 * scale)));
		g.translate(-game_upper_left.x * scale, -game_upper_left.y * scale);
		g.scale(scale, scale);		
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		curr_mouse_position = new Vec2f(e.getX(), e.getY());
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		curr_mouse_position = new Vec2f(e.getX(), e.getY());
		dragging = true;
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		dragging = false;
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() < 0) {
			scale = scale * 1.1f;
		} else {
			scale = scale * 0.9f;
		}
		scale = scale > max_scale ? max_scale : scale;
		scale = scale < min_scale ? min_scale : scale;	
	}

	public Vec2f getWorldMouseLocation() {
		Vec2f temp_ml = new Vec2f(curr_mouse_position.x, curr_mouse_position.y);
		Vec2f game_upper_left = center_position.minus(new Vec2f(this.attrSize.x/(2 * scale), this.attrSize.y/(2 * scale)));
		temp_ml = new Vec2f(temp_ml.x + game_upper_left.x * scale, temp_ml.y + game_upper_left.y * scale);
		temp_ml = new Vec2f(temp_ml.x/scale, temp_ml.y/scale);
		return temp_ml;
	}

	public Vec2f toScreenCoords(Vec2f v) {
		Vec2f temp_ml = new Vec2f(v.x, v.y);
		Vec2f game_upper_left = center_position.minus(new Vec2f(this.attrSize.x/(2 * scale), this.attrSize.y/(2 * scale)));
		temp_ml = new Vec2f(temp_ml.x * scale, temp_ml.y * scale);
		temp_ml = new Vec2f(temp_ml.x - game_upper_left.x * scale, temp_ml.y - game_upper_left.y * scale);
		return temp_ml;
	}

	public Vec2i getMouseGridPosition() {
		Vec2f wml = getWorldMouseLocation();
		Vec2i gridpos = new Vec2i((int)Math.floor(wml.x / rendered_universe.getGridwidth()), (int)Math.floor(wml.y / rendered_universe.getGridwidth()));
		return gridpos;
	}
}