package iebaker.wt.screens;

import iebaker.xenon.core.Application;
import iebaker.xenon.core.Widget;
import iebaker.xenon.core.Artist;
import iebaker.xenon.core.Screen;
import iebaker.xenon.core.BackgroundWidget;
import iebaker.xenon.core.ImageWidget;
import iebaker.xenon.slice.Node;
import iebaker.xenon.slice.Slice;
import iebaker.xenon.slice.ChildNotFoundException;

import iebaker.wt.world.WTWorld;

import cs195n.Vec2i;

import java.awt.event.MouseEvent;
import java.awt.Color;

public class WTResultScreen extends Screen {

	private final String BACKGROUND = "wt.resultscreen.background";
	private final String RESULT 	= "wt.resultscreen.result";
	private final String NODE 		= "wt.resultscreen.node";
	private final String NEWGAME 	= "wt.resultscreen.newgame";
	private final String QUIT		= "wt.resultscreen.quit";

	private final String WOOD 		= "lib/img/wood.png";
	private final String GOODJOB	= "lib/img/WT_goodjob.png";
	private final String NEWGAMEP 	= "lib/img/WT_newgame.png";
	private final String QUITP 		= "lib/img/WT_quit.png";

	public WTResultScreen(WTWorld.Result r, Application a, String id) {
		super(a, id);

		BackgroundWidget background = new BackgroundWidget(this, BACKGROUND);
		background.setBGPaint(Color.WHITE);

		ImageWidget result = new ImageWidget(this, RESULT);
		if(r == WTWorld.Result.WIN) {
			result.setImage(GOODJOB);
		} else {
			result.setImage("WT_die_result.png");
		}

		ImageWidget new_game = new ImageWidget(this, NEWGAME) {
			@Override public void clickBehavior(MouseEvent event, Application app, Screen screen) {
				app.getScreenManager().pushScreen(new WTMainScreen(app, "wt.mainscreen"));
				app.getScreenManager().removeScreen(screen);
			}
		};
		new_game.setImage(NEWGAMEP);
		//new_game.setHoverImage(imgdir + "WT_newgame_hover.png");

		ImageWidget quit = new ImageWidget(this, QUIT) {
			@Override public void clickBehavior(MouseEvent event, Application app, Screen screen) {
				System.exit(0);
			}
		};
		quit.setImage(QUITP);
		//quit.setHoverImage(imgdir + "WT_help_hover.png");

		registerWidgets(background, result, new_game, quit);

		root_node = new Node(my_app, this, NODE) {
			@Override public void build(Vec2i newSize) {
				super.build(newSize);

				Widget background = my_screen.getWidget(BACKGROUND);
				Widget result = my_screen.getWidget(RESULT);
				Widget new_game = my_screen.getWidget(NEWGAME);
				Widget quit = my_screen.getWidget(QUIT);

				try {
					this.attach(background);

					this.divide(new Slice()
						{{ type(T.FLOATING); direction(D.VERT); sizes(135f); names("pre_centerblock"); }} );

					this.getChild("pre_centerblock").divide(new Slice()
						{{ type(T.FLOATING); direction(D.HORZ); sizes(430f); names("centerblock"); }} );

					Node centerblock = this.getChild("pre_centerblock", "centerblock");

					centerblock.divide(new Slice()
						{{ type(T.BASIC); direction(D.VERT); splits(0.75f); names("title", "menu"); }} );

					centerblock.getChild("title").attach(result);

					Node menu = centerblock.getChild("menu");

					menu.divide(new Slice()
						{{ type(T.FLOATING); direction(D.HORZ); sizes(100f, 200f); names("quit", "newgame"); }} );

					menu.getChild("quit").attach(quit);
					menu.getChild("newgame").attach(new_game);
					
				} catch (ChildNotFoundException e) {
					System.err.println("Error making layout for WTResultScreen (this should never happen)");
					e.printStackTrace();
				}
			}
		};

		root_node.build(my_app.getSize());
	}
}
