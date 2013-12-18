package iebaker.tou.screens;

import iebaker.krypton.core.Widget;
import iebaker.krypton.core.Application;
import iebaker.krypton.core.Screen;
import iebaker.krypton.core.widgets.BackgroundWidget;
import iebaker.krypton.core.widgets.TextLabel;
import iebaker.krypton.core.widgets.TextButton;
import iebaker.krypton.slice.Node;
import iebaker.krypton.slice.Slice;
import iebaker.krypton.slice.ChildNotFoundException;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.Paint;

import cs195n.Vec2i;

public class TouEndScreen extends Screen {
	public TouEndScreen(Application a, String id, int score) {
		super(a, id);

		BackgroundWidget background = new BackgroundWidget(parent_application, this, "touendscreen.background");
		background.setBGPaint(Color.BLACK);

		TextLabel title = new TextLabel(parent_application, this, "touendscreen.title");
		title.setText("You died :(").setFontSize(40).setBGPaint(Color.GRAY);

		TextButton play = new TextButton(parent_application, this, "touendscreen.play") {
			@Override
			public void clickBehavior(MouseEvent mouse_event, Application application, Screen screen) {
				//TouGUI gui = (TouGUI) application.getScreenManager().getScreenByID("tou.tougui");
				//gui.leave();
				application.getScreenManager().pushScreen(new TouMainScreen(application, "tou.mainscreen"));
			}
		};
		play.setText("any key to continue").setFontSize(40).setNormalPaint(Color.GRAY).setHighlightPaint(Color.RED);

		registerWidgets(background, title, play);

		root_node = new Node(parent_application, this, "toumainscreen.root") {
		
		@Override public void build(Vec2i newSize) {
			super.build(newSize);

			Widget background = getWidgetByID("touendscreen.background");
			Widget title = getWidgetByID("touendscreen.title");
			Widget play = getWidgetByID("touendscreen.play");

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
				System.err.println("Layout build failure");
			}
		}};

		root_node.build(parent_application.getSize());
	}


	@Override
	public void onKeyPressed(KeyEvent e) {
		parent_application.getScreenManager().clearScreens();
		parent_application.getScreenManager().pushScreen(new TouMainScreen(parent_application, "tou.main"));
		//parent_application.getScreenManager().removeScreen(this);
		//parent_application.getScreenManager().removeScreen(parent_application.getScreenManager().getScreenByID("tou.tougui"));
	}
}
