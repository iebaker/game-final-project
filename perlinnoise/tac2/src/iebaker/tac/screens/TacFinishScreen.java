package iebaker.tac.screens;

import iebaker.argon.core.*;
import iebaker.argon.slice.*;
import iebaker.argon.world.*;
import iebaker.tac.widgets.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Paint;

import cs195n.*;


public class TacFinishScreen extends Screen {
	public TacFinishScreen(Application a, String id, int res) {
		super(a, id);
		BackgroundWidget background = new BackgroundWidget(parent_application, this, "bg");
		String asdf = res == 0 ? "Stones won" : "Sticks won";
		TitleWidget title = new TitleWidget(parent_application, this, "tit", asdf);
		registerWidgets(background, title);

		root_node = new Node(parent_application, this, "root") {
			@Override
			public void build(Vec2i newSize) {
				super.build(newSize);

				Widget background = m_parent_screen.getWidgetByID("bg");
				Widget title = m_parent_screen.getWidgetByID("tit");

				try {
					this.attach(background);

					this.divide(new Slice()
						{{
							type(T.FLOATING); direction(D.VERT); sizes(400f);
						}});

					this.getChild("0").divide(new Slice()
						{{
							type(T.FLOATING); direction(D.HORZ); sizes(400f);
						}});

					this.getChild("0", "0").attach(title);
				} catch (ChildNotFoundException e) {
					System.out.println("Figgity fuck");
				}
			}
		};
		root_node.build(parent_application.getSize());
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		parent_application.getScreenManager().pushScreen(new TacMainScreen(parent_application, "tac.main"));
		parent_application.getScreenManager().removeScreen(this);
	}
}