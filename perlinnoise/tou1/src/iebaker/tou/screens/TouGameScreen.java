package iebaker.tou.screens;

import iebaker.krypton.core.Screen;
import iebaker.krypton.core.Widget;
import iebaker.krypton.core.Application;
import iebaker.krypton.core.widgets.BackgroundWidget;
import iebaker.krypton.slice.Slice;
import iebaker.krypton.slice.Node;
import iebaker.krypton.slice.ChildNotFoundException;
import iebaker.tou.world.TouWorld;
import java.awt.Graphics2D;
import java.awt.Color;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class TouGameScreen extends Screen {
	private TouWorld world;

	public TouGameScreen(Application a, String id) {
		super(a, id);

		BackgroundWidget background = new BackgroundWidget(parent_application, this, "tougamescreen.background");
		background.setBGPaint(Color.WHITE);
		registerWidgets(background);

		root_node = new Node(parent_application, this, "tougamescreen.root") {
			@Override public void build(Vec2i newSize) {
				super.build(newSize);

				Widget background = m_parent_screen.getWidgetByID("tougamescreen.background");
				this.attach(background);
			}
		};

		root_node.build(parent_application.getSize());

		world = new TouWorld(new Vec2f(parent_application.getSize()));

		parent_application.getScreenManager().pushScreen(new TouGUI(parent_application, "tou.tougui", world));
	}

	@Override
	public void onDraw(Graphics2D g) {
		world.onDraw(g);
	}

	@Override
	public void onTick(long nanos) {
		world.onTick(nanos);
	}
}	
