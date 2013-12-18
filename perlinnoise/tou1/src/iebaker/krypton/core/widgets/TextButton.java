package iebaker.krypton.core.widgets;

import iebaker.krypton.core.Application;
import iebaker.krypton.core.Screen;
import iebaker.krypton.core.Widget;

import java.awt.Paint;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import cs195n.Vec2i;

public class TextButton extends Widget {
	public String button_text = "";
	private Vec2i mouse_position = new Vec2i(0,0);
	private boolean mouse_over = false;
	private Paint highlight = new Color(0.5f, 0.5f, 0.5f);
	private Paint normal = new Color(0.3f, 0.3f, 0.3f);
	private int my_font_size = 10;
	private String my_button_text = "";

	public TextButton(Application a, Screen s, String id) {
		super(a, s, id);
	}

	public void onMouseClicked(MouseEvent e) {
		if(mouse_over) {
			this.clickBehavior(e, my_application, my_screen);
		}
	}

	@Override
	public void onTick(long nanos) {
		checkMousePosition();
	}

	public void clickBehavior(MouseEvent e, Application a, Screen s) {
		return;
	}

	private void checkMousePosition() {
		if(mouse_position.x >= attrLocation.x && mouse_position.x <= attrLocation.x + attrSize.x
			&& mouse_position.y >= attrLocation.y && mouse_position.y <= attrLocation.y + attrSize.y) {
			mouse_over = true;
		} else {
			mouse_over = false;
		}
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		mouse_position = new Vec2i(e.getX(), e.getY());
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		mouse_position = new Vec2i(e.getX(), e.getY());
	}

	@Override
	public void onDraw(Graphics2D g) {
		a.strokeOff();
		a.setFillPaint(mouse_over ? highlight : normal);
		a.roundrect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y, 10, 10);
		a.setFillPaint(Color.WHITE);
		a.setTextAlign(a.CENTER, a.CENTER);
		a.setFontSize(my_font_size);
		a.text(g, my_button_text, this.getHCenter(), this.getVCenter());
	}

	public TextButton setText(String text) {
		my_button_text = text;
		return this;
	}

	public TextButton setFontSize(int size) {
		my_font_size = size;
		return this;
	}

	public TextButton setNormalPaint(Paint p) {
		normal = p;
		return this;
	}

	public TextButton setHighlightPaint(Paint p) {
		highlight = p;
		return this;
	}
}
