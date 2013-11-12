package iebaker.wt.screens;

import iebaker.xenon.core.Application;
import iebaker.xenon.core.Screen;
import iebaker.xenon.core.Artist;
import iebaker.xenon.core.Widget;
import iebaker.xenon.core.BackgroundWidget;
import iebaker.xenon.core.Viewport;
import iebaker.xenon.slice.Node;
import iebaker.xenon.slice.Slice;
import iebaker.xenon.slice.ChildNotFoundException;

import cs195n.Vec2i;
import cs195n.Vec2f;

import iebaker.wt.world.WTWorld;
import iebaker.wt.world.Player;

import java.awt.event.KeyEvent;
import java.awt.Color;

public class WTGameScreen extends Screen {

	private WTWorld my_world;

	private final String 		BACKGROUND 	= "wt.gamescreen.background";
	private final String		VIEWPORT 	= "wt.gamescreen.viewport";
	private final String 		LEVELINFO 	= "wt.gamescreen.levelinfo";
	private final String		WOOD		= "lib/img/wood.png";
	private final String		GEOMETRY	= "lib/img/geometry.png";
	private final String		BARK		= "lib/img/tree_bark.png";
	private final String		NODE		= "wt.gamescreen.root";

	public WTGameScreen(Application a, String id) {
		super(a, id);

		my_world = new WTWorld(new Vec2f(600, 600));
		my_world.init();

		BackgroundWidget background = new BackgroundWidget(this, BACKGROUND);
		background.setBGPaint(Color.BLACK);

		Viewport viewport = new Viewport(this, VIEWPORT);
		viewport.setBGPaint((new Artist()).makeTexturePaint(BARK));
		viewport.init(my_world);

		registerWidgets(background, viewport);

		root_node = new Node(my_app, this, NODE) {
			@Override public void build(Vec2i newSize) {
				super.build(newSize);
			
				Widget background = my_screen.getWidget(BACKGROUND);
				Widget viewport = my_screen.getWidget(VIEWPORT);

				this.attach(background);

				this.setPadding(10);
				
				this.divide(
					new Slice() {{ type(T.BASIC); direction(D.VERT); splits(1f); names("game", "levelinfo"); }} );
			
				try {
					this.getChild("game").attach(viewport);
				} catch(ChildNotFoundException e) {
					System.out.println("Failed to build layout for WTGameScreen (this should never happen)");
				}
			}
		};

		root_node.build(my_app.getSize());
	}

	@Override
	public void onTick(long nanos) {
		my_world.update(nanos/1E9f);
		if(my_world.gameOver()) {
			WTResultScreen wtr = new WTResultScreen(WTWorld.Result.WIN, my_app, "wt.resultscreen");
			my_app.getScreenManager().pushScreen(wtr);
			my_app.getScreenManager().removeScreen(this);
			return;
		}
		Vec2f rp_loc = my_world.getRedPlayer().getPosition();
		Vec2f bp_loc = my_world.getBluePlayer().getPosition();

		Vec2f avg = new Vec2f((rp_loc.x + bp_loc.x)/2, (rp_loc.y + bp_loc.y)/2);
		float dist = rp_loc.dist(bp_loc);
		dist = dist < 200 ? 200 : dist;
		Viewport vp = (Viewport) getWidget(VIEWPORT);
		vp.getCamera().setFocus(avg);
		vp.getCamera().setZoom(20/((float)Math.sqrt(dist) + 1));
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		if(my_world.gameOver()) return;
		Player redPlayer = my_world.getRedPlayer();
		Player bluePlayer = my_world.getBluePlayer();

		switch(e.getKeyChar()) {
			case 'a':
				redPlayer.moveLeft();
				break;

			case 'd':
				redPlayer.moveRight();
				break;

			case 'w':
				redPlayer.jump();
				break;

			case 'q':
				redPlayer.jumpBall(-1);
				break;

			case 'e':
				redPlayer.jumpBall(1);
				break;

			case 'j':
				bluePlayer.moveLeft();
				break;

			case 'l':
				bluePlayer.moveRight();
				break;

			case 'i':
				bluePlayer.jump();
				break;

			case 'u':
				bluePlayer.jumpBall(-1);
				break;

			case 'o':
				bluePlayer.jumpBall(1);
				break;

			case ' ':
				my_world.restartLevel();
				break;
		}
	}
}
