package iebaker.tac.screens;

import iebaker.argon.core.*;
import iebaker.argon.slice.*;
import iebaker.argon.world.*;
import iebaker.tac.widgets.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Paint;

import cs195n.*;

public class TacMainScreen extends Screen {
	public TacMainScreen(Application a, String id) {
		super(a, id);
		BackgroundWidget background = new BackgroundWidget(parent_application, this, "tacmainscreen.background");
		TitleWidget title = new TitleWidget(parent_application, this, "tacmainscreen.title");
		PlayButton play = new PlayButton(parent_application, this, "tacmainscreen.play");
		registerWidgets(background, title, play);

		root_node = new Node(parent_application, this, "tacmainscreen.root") {
			@Override
			public void build(Vec2i newSize) {
				super.build(newSize);

				Widget background = m_parent_screen.getWidgetByID("tacmainscreen.background");
				Widget title = m_parent_screen.getWidgetByID("tacmainscreen.title");
				Widget play = m_parent_screen.getWidgetByID("tacmainscreen.play");

				try {
					this.attach(background);
					
					this.divide(new Slice()
						{{ type(T.FLOATING); direction(D.VERT); sizes(400f); }} );

					this.getChild("0").divide(new Slice()
						{{ type(T.FLOATING); direction(D.HORZ); sizes(400f); }} );

					this.getChild("0", "0").divide(new Slice()
						{{ type(T.BASIC); splits(0.25f); names("top", "bottom"); padding(10f); }} );

					this.getChild("0", "0", "top").attach(title);
					this.getChild("0", "0", "bottom").attach(play);

				} catch (ChildNotFoundException e) {
					System.out.println("Layout build failure");
				}
			}
		};

		root_node.build(parent_application.getSize());
	}
}
