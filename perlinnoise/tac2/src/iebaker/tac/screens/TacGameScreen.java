package iebaker.tac.screens;

import cs195n.*;
import iebaker.argon.world.Viewport;
import iebaker.argon.world.GridGraph;
import iebaker.argon.world.Viewport;
import iebaker.argon.core.Application;
import iebaker.argon.core.Screen;
import iebaker.argon.core.Widget;
import iebaker.argon.world.PerlinSampler;
import iebaker.tac.world.TacUniverse;
import iebaker.tac.world.StickSoldier;
import iebaker.tac.world.StickBuilder;
import iebaker.tac.widgets.BackgroundWidget;
import java.awt.event.KeyEvent;

public class TacGameScreen extends Screen {



	private TacUniverse 	universe;
	private Viewport 		view;



	public TacGameScreen(Application a, String id) {
		super(a, id);

		GridGraph graph = new GridGraph(50, 50);
		universe = new TacUniverse(graph, 200f, 20);
		universe.addEntityClass(StickSoldier.class);
		universe.addEntityClass(StickBuilder.class);

		BackgroundWidget background = new BackgroundWidget(parent_application, this, "tacgamescreen.background");
		view = new Viewport(parent_application, this, "tacgamescreen.viewport", universe);
		parent_application.getScreenManager().pushScreen(new TacGUI(parent_application, "tac.tacgui", universe, view));
		registerWidgets(background, view);

		root_node = new iebaker.argon.slice.Node(parent_application, this, "tacgamescreen.root") {

			@Override public void build(Vec2i newSize) {

				super.build(newSize);

				Widget background = m_parent_screen.getWidgetByID("tacgamescreen.background");
				Widget view = m_parent_screen.getWidgetByID("tacgamescreen.viewport");

				this.attach(background);
				this.attach(view);

			}
		};

		root_node.build(parent_application.getSize());
	}




	@Override
	public void onKeyTyped(KeyEvent e) {
		if(e.getKeyChar() == 'n') {
			this.setActive(false);
			GridGraph graph = new GridGraph(50, 50);
			universe = new TacUniverse(graph, 200f, 20);
			this.setActive(true);
		}
	}	
}