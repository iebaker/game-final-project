package iebaker.tac.widgets;

import iebaker.argon.core.*;
import iebaker.tac.screens.*;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Color;
import java.awt.event.*;
import cs195n.*;

public class PlayButton extends Widget {
	private Vec2i mousePosition = new Vec2i(0,0);
	private boolean mouseOver = false;
	private Paint highlight = new Color(0.5f, 0.5f, 0.5f);
	private Paint normal = new Color(0.3f, 0.3f, 0.3f);

	public PlayButton(Application a, Screen parent, String id) {
		super(a, parent, id);
	}

	@Override
	public void onTick(long nanos) {
		checkMousePosition();
	}

	private void checkMousePosition() {
		if(mousePosition.x >= attrLocation.x && mousePosition.x <= attrLocation.x + attrSize.x
			&& mousePosition.y >= attrLocation.y && mousePosition.y <= attrLocation.y + attrSize.y) {
			mouseOver = true;
		} else {
			mouseOver = false;
		}
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		mousePosition = new Vec2i(e.getX(), e.getY());
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		mousePosition = new Vec2i(e.getX(), e.getY());
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		if(mouseOver) {
			ScreenManager sm = my_application.getScreenManager();
			sm.pushScreen(new TacGameScreen(my_application, "tac.gamescreen"));
			sm.removeScreen(my_screen);
			sm.rotateUp();
		}
	}

	@Override
	public void onDraw(Graphics2D g) {
		a.strokeOff();
		a.setFillPaint(mouseOver ? highlight : normal);
		a.roundrect(g, attrLocation.x, attrLocation.y, attrSize.x, attrSize.y, 10, 10);
		a.setFillPaint(Color.WHITE);
		a.setTextAlign(a.CENTER, a.CENTER);
		a.setFontSize(40);
		a.text(g, "Play!", this.getHCenter(), this.getVCenter());
	}
}
