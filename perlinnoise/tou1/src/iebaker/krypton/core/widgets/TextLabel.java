package iebaker.krypton.core.widgets;

import iebaker.krypton.core.Application;
import iebaker.krypton.core.Screen;
import iebaker.krypton.core.Widget;

import java.awt.Paint;
import java.awt.Color;
import java.awt.Graphics2D;

public class TextLabel extends Widget {
	private String my_label_text = "";
	private Paint my_background_paint = Color.BLACK;
	private int my_font_size = 10;
	private Paint my_text_color = Color.WHITE;

	public TextLabel(Application a, Screen parent, String id) {
		super(a, parent, id);
	}

	@Override
	public void onDraw(Graphics2D g) {
		a.strokeOff();
		a.setTextAlign(a.CENTER, a.CENTER);
		a.setFontSize(my_font_size);
		a.setFillPaint(my_background_paint);
		a.roundrect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y, 10f, 10f);
		a.setFillPaint(my_text_color);
		a.text(g, my_label_text, this.getHCenter(), this.getVCenter());
	}

	public TextLabel setText(String text) {
		my_label_text = text;
		return this;
	}

	public TextLabel setBGPaint(Paint p) {
		my_background_paint = p;
		return this;
	}

	public TextLabel setFontSize(int size) {
		my_font_size = size;
		return this;
	}

	public TextLabel setTextPaint(Paint p) {
		my_text_color = p;
		return this;
	}
}
