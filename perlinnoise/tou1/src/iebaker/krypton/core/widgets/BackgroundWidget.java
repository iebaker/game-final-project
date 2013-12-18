package iebaker.krypton.core.widgets;

import iebaker.krypton.core.Application;
import iebaker.krypton.core.Screen;
import iebaker.krypton.core.Widget;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Paint;

public class BackgroundWidget extends Widget {
	private Paint my_bgpaint;

	public BackgroundWidget(Application a, Screen parent, String id) {
		super(a, parent, id);
	}

	@Override
	public void onDraw(Graphics2D g) {
		a.strokeOff();
		a.setFillPaint(my_bgpaint);
		a.rect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y);
	}

	public BackgroundWidget setBGPaint(Paint p) {
		my_bgpaint = p;
		return this;
	}
}

