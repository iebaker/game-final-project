package iebaker.tac.widgets;

import cs195n.*;
import java.util.ArrayList;
import java.lang.Class;
import java.awt.Paint;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import iebaker.tac.screens.TacGUI;
import iebaker.tac.world.TacUniverse;
import iebaker.argon.core.Widget;
import iebaker.argon.core.Application;
import iebaker.argon.core.Screen;
import iebaker.argon.world.Entity;

public class EntityMenu extends Widget {
	private TacGUI gui;
	private TacUniverse my_tac_universe;
	private java.util.List<MenuButton> my_buttons = new ArrayList<MenuButton>();
	private MenuButton hovered_button;
	private MenuButton selected_button;
	private Paint normal_color = Color.WHITE;
	private Paint selected_color = new Color(100,200,100);
	private Paint hovered_color = new Color(100,100,200);
	private Paint background_color = Color.GRAY;
	private float button_height = 40;
	private Vec2i mousePosition = new Vec2i(0,0);

	public class MenuButton {
		private Class<?> represented_class;
		private String button_text;

		public MenuButton(Class<?> ce, String bt) {
			represented_class = ce;
			button_text = bt;
		}

		public Class<?> getRepClass() {
			return represented_class;
		}

		public String getText() {
			return button_text;
		}
	}

	public EntityMenu(Application a, Screen parent, String id, TacUniverse tu) {
		super(a, parent, id);
		gui = (TacGUI) parent;
		my_tac_universe = tu;
		populate();
	}

	private void populate() {
		System.out.println(my_tac_universe);
		for(Class<?> ecl : my_tac_universe.getEntityClasses()) {
			this.addMenuButton(ecl, ecl.getName());
		}
	}

	private void addMenuButton(Class<?> ce, String bt) {
		my_buttons.add(new MenuButton(ce, bt));
	}

	public void onTick(long nanos) {
		hovered_button = checkMousePosition();
	}

	public MenuButton checkMousePosition() {
		if(mousePosition.x >= attrLocation.x && mousePosition.x <= attrLocation.x + attrSize.x) {
			float dist = mousePosition.y - attrLocation.y;
			int index = (int) (dist/this.button_height);
			if(index >= 0 && index < my_buttons.size()) {
				return my_buttons.get(index);
			}
		}
		return null;
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		MenuButton mb = checkMousePosition();
		if(mb != null) {
			selected_button = mb;
			gui.setDrawingClass(mb.getRepClass());
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
	public void onDraw(Graphics2D g) {
		a.strokeOff();
		a.setFillPaint(new Color(0f, 0f, 0f, 0.05f));
		for(int i = 0; i < 10; ++i) {
			a.rect(g, this.attrLocation.x + this.attrSize.x - i, this.attrLocation.y, 10f, this.attrSize.y);
		}
		a.rect(g, this.attrLocation.x, this.attrLocation.y, this.attrSize.x + 10, this.attrSize.y);
		a.setFillPaint(background_color);
		a.setTextAlign(a.CENTER, a.CENTER);
		a.rect(g, this.attrLocation.x, this.attrLocation.y, this.attrSize.x, this.attrSize.y);
		int i = 0;
		for(MenuButton mb : my_buttons) {
			if(mb == hovered_button) {
				a.setFillPaint(selected_color);
			} else if(mb == selected_button) {
				a.setFillPaint(hovered_color);
			} else {
				a.setFillPaint(normal_color);
			}
			a.rect(g, this.attrLocation.x, this.attrLocation.y + i * button_height, this.attrSize.x, button_height);
			a.setFillPaint(Color.BLACK);
			a.text(g, mb.getText(), this.getHCenter(), this.attrLocation.y + i * button_height + button_height/2);
			i++;			
		}
	}
}