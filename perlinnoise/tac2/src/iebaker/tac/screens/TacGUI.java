package iebaker.tac.screens;

import cs195n.*;
import iebaker.argon.core.Screen;
import iebaker.argon.core.Application;
import iebaker.argon.core.Widget;
import iebaker.argon.core.Artist;
import iebaker.argon.slice.Slice;
import iebaker.argon.slice.ChildNotFoundException;
import iebaker.argon.slice.Node;
import iebaker.argon.world.Entity;
import iebaker.argon.world.Viewport;
import iebaker.argon.world.Place;
import iebaker.tac.world.TacUniverse;
import iebaker.tac.world.TacCreature;
import iebaker.tac.world.EntityFactory;
import iebaker.tac.widgets.EntityMenu;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.event.MouseEvent;

public class TacGUI extends Screen {
	private TacUniverse my_tac_universe;
	private Class<?> current_entity_drawing_class;
	private Viewport sister_viewport;
	private Vec2i mouse_in_world;
	private Artist a = new Artist();
	
	public TacGUI(Application a, String id, TacUniverse u, Viewport v) {
		super(a, id);
		sister_viewport = v;
		my_tac_universe = u;

		EntityMenu em = new EntityMenu(parent_application, this, "tacgui.entitymenu", my_tac_universe);
		registerWidgets(em);

		root_node = new Node(parent_application, this, "tacgui.root") {

			@Override
			public void build(Vec2i newSize) {
				super.build(newSize);

				Widget menu = m_parent_screen.getWidgetByID("tacgui.entitymenu");

				try {
					this.setPadding(15f);
					this.divide(new Slice()
						{{ type(T.BASIC); direction(D.HORZ); splits(0.25f); names("left", "right"); }} );

					this.getChild("left").attach(menu);

				} catch (ChildNotFoundException e) {
					System.err.println("Error building TacGUI layout");
				}
			}
 		};

 		root_node.build(parent_application.getSize());
	}

	public TacUniverse getTacUniverse() {
		return my_tac_universe;
	}

	public void setDrawingClass(Class<?> ent_class) {
		current_entity_drawing_class = ent_class;
	}

	@Override
	public void onTick(long nanos) {
		super.onTick(nanos);
		mouse_in_world = sister_viewport.getMouseGridPosition();
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		Place world_place = new Place(mouse_in_world.x, mouse_in_world.y);
		if(my_tac_universe.onMouseClicked(my_tac_universe.getGridGraph().get(world_place), e)) {
			return;
		}
		if(current_entity_drawing_class != null) {
			if(my_tac_universe.getGridGraph().isRealPlace(world_place) && e.getButton() == MouseEvent.BUTTON1) {
				my_tac_universe.addEntity(my_tac_universe.getGridGraph().get(world_place), (Entity)EntityFactory.spawn(current_entity_drawing_class));
			}
		}
		super.onMouseClicked(e);
	}

	@Override
	public void onDraw(Graphics2D g) {
		if(my_tac_universe != null && sister_viewport != null && mouse_in_world != null) {

			//Draw little red box over mouse location
			AffineTransform preserved = g.getTransform();
			sister_viewport.setToWorldCoords(g);
			float gw = my_tac_universe.getGridwidth();
			a.strokeOff();
			a.setFillPaint(new Color(1f, 0f, 0f, 0.5f));
			a.rect(g, mouse_in_world.x * gw, mouse_in_world.y * gw, gw, gw);
			g.setTransform(preserved);
			Entity e = my_tac_universe.getSelectedEntity();

			//Draw on required non-scaling element
			if(e != null && e.getPlace() != null) {
				Vec2f loc = new Vec2f((e.getPlace().getX()) * gw, (e.getPlace().getY()) * gw);
				Vec2f conv = sister_viewport.toScreenCoords(loc);
				a.setFillPaint(new Color(1f, 1f, 1f, 0.8f));
				a.roundrect(g, conv.x, conv.y - 35, 100, 30, 10, 10);
				a.setTextAlign(a.CENTER, a.CENTER);
				a.setFillPaint(Color.BLACK);
				if(e instanceof TacCreature) a.text(g, "Health: " + ((TacCreature)e).getHealth(), conv.x + 50, conv.y - 35 + 15);
			}

			super.onDraw(g);
		}
	}
}