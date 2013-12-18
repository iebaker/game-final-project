package iebaker.tou.screens;

import iebaker.krypton.core.Screen;
import iebaker.krypton.core.Widget;
import iebaker.krypton.core.Application;
import iebaker.krypton.core.Viewport;
import iebaker.krypton.core.widgets.BackgroundWidget;
import iebaker.krypton.slice.Slice;
import iebaker.krypton.slice.Node;
import iebaker.krypton.slice.ChildNotFoundException;
import iebaker.tou.world.TouWorld;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class TouGameScreen extends Screen {
	private TouWorld world;
	private Viewport view;

	public TouGameScreen(Application a, String id) {
		super(a, id);

		BackgroundWidget background = new BackgroundWidget(parent_application, this, "tougamescreen.background");
		background.setBGPaint(Color.GRAY);
		view = new Viewport(this, "view");
		registerWidgets(background, view);

		root_node = new Node(parent_application, this, "tougamescreen.root") {
			@Override public void build(Vec2i newSize) {
				super.build(newSize);

				Widget background = m_parent_screen.getWidgetByID("tougamescreen.background");
				Widget view = m_parent_screen.getWidgetByID("view");

				this.attach(background);
				this.attach(view);
			}
		};

		root_node.build(parent_application.getSize());

		world = new TouWorld(new Vec2f(parent_application.getSize()));
		view.init(world);

		parent_application.getScreenManager().pushScreen(new TouGUI(parent_application, "tou.tougui", world));
	}

	@Override
	public void onTick(long nanos) {
		Viewport.Camera c = view.getCamera();
		c.setFocus(world.getPlayer().getPosition());
		world.onTick(nanos);
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		Viewport.Camera c = view.getCamera();
		if(e.getWheelRotation() < 0) {
			c.zoom(1.1f);
		} else {
			c.zoom(0.9f);
		}
	}

}	
