package iebaker.tac.widgets;

import iebaker.argon.core.*;
import java.awt.Graphics2D;
import java.awt.Color;

public class TitleWidget extends Widget {
	public TitleWidget(Application a, Screen parent, String id) {
		super(a, parent, id);
	}

	@Override
	public void onDraw(Graphics2D g) {
		//System.out.println("Loc " + attrLocation.x + ", " + attrLocation.y + "Siz " + attrSize.x + ", " + attrSize.y);
		a.strokeOff();
		a.setTextAlign(a.CENTER, a.CENTER);
		a.setFontSize(20);
		a.setFillPaint(new Color(0.5f, 0.5f, 0.5f));
		a.roundrect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y, 10f, 10f);
		a.setFillPaint(Color.WHITE);
		a.text(g, "Lonely Forest", this.getHCenter(), this.getVCenter());
	}
}
