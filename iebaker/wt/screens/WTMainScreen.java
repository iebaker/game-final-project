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

import cs195n.Vec2i;

import java.awt.event.MouseEvent;
import java.awt.Color;

public class WTMainScreen extends Screen {

	private final String imgdir = "lib/img/";

	private final String BACKGROUND = "wt.mainscreen.background";
	private final String TITLE		= "wt.mainscreen.title";
	private final String NEWGAME 	= "wt.mainscreen.newgame";
	private final String QUIT  		= "wt.mainscreen.quit";
	private final String HELP 		= "wt.mainscreen.help";
	private final String NODE  		= "wt.mainscreen.node";

	/**
	 * Constructor.
	 */
	public WTMainScreen(Application a, String id) {
		super(a, id);

		//Create the background widget
		BackgroundWidget background = new BackgroundWidget(this, BACKGROUND);
		background.setBGPaint(Color.WHITE);

		//Create the title widget
		ImageWidget title = new ImageWidget(this, TITLE);
		title.setImage(imgdir + "WT_logo.png");

		//Create the new game button
		ImageWidget new_game = new ImageWidget(this, NEWGAME) {
			@Override public void clickBehavior(MouseEvent event, Application app, Screen screen) {
				app.getScreenManager().pushScreen(new WTGameScreen(app, "wt.gamescreen"));
				app.getScreenManager().removeScreen(screen);
			}
		};
		new_game.setImage(imgdir + "WT_newgame.png");
		//new_game.setHoverImage(imgdir + "WT_newgame_hover.png");

		//Create the quit button
		ImageWidget quit = new ImageWidget(this, QUIT) {
			@Override public void clickBehavior(MouseEvent event, Application app, Screen screen) {
				System.exit(0);
			}
		};
		quit.setImage(imgdir + "WT_quit.png");
		//quit.setHoverImage(imgdir + "WT_quit_hover.png");

		//Create the info button
		ImageWidget help = new ImageWidget(this, HELP) {
			@Override public void clickBehavior(MouseEvent event, Application app, Screen screen) {
				app.getScreenManager().pushScreen(new WTHelpScreen(app, "wt.infoscreen"));
			}
		};
		help.setImage(imgdir + "WT_help.png");
		//help.setHoverImage(imgdir + "WT_help_hover.png");

		registerWidgets(background, title, new_game, quit, help);

		root_node = new Node(my_app, this, NODE) {
			@Override public void build(Vec2i newSize) {
				super.build(newSize);

				Widget background = my_screen.getWidget(BACKGROUND);
				Widget title = my_screen.getWidget(TITLE);
				Widget new_game = my_screen.getWidget(NEWGAME);
				Widget quit = my_screen.getWidget(QUIT);
				Widget help = my_screen.getWidget(HELP);

				try {	
					this.attach(background);

					this.divide(new Slice() 
						{{ type(T.FLOATING); direction(D.VERT); sizes(135f); names("pre_centerblock"); }} );

					this.getChild("pre_centerblock").divide(new Slice()
						{{ type(T.FLOATING); direction(D.HORZ); sizes(479f); names("centerblock"); }} );

					Node centerblock = this.getChild("pre_centerblock", "centerblock");

					centerblock.divide(new Slice()
						{{ type(T.BASIC); direction(D.VERT); splits(0.75f); names("title", "menu"); }} );

					centerblock.getChild("title").attach(title);

					Node menu = centerblock.getChild("menu");

					menu.divide(new Slice()
						{{ type(T.FLOATING); direction(D.HORZ); sizes(100f, 200f, 100f); names("quit", "newgame", "help"); }} );

					menu.getChild("quit").attach(quit);
					menu.getChild("newgame").attach(new_game);
					menu.getChild("help").attach(help);

				} catch (ChildNotFoundException e) {
					System.err.println("Error making layout for WTMainScreen! (this should never happen)");
					e.printStackTrace();
				}
			}
		};

		root_node.build(my_app.getSize());
	}
}
