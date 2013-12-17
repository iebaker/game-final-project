package iebaker.tac.widgets;

import iebaker.argon.core.*;
import java.awt.Graphics2D;
import java.awt.Color;

public class BackgroundWidget extends Widget {
	public BackgroundWidget(Application a, Screen parent, String id) {
		super(a, parent, id);
	}

	@Override
	public void onDraw(Graphics2D g) {
		a.strokeOff();
		a.setFillPaint(new Color(50, 50, 50));
		a.rect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y);
	}
}
