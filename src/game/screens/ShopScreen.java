package game.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import cs195n.Vec2f;
import cs195n.Vec2i;
import engine.Application;
import engine.Screen;
import engine.ui.UIButton;
import engine.ui.UIRect;
import engine.ui.UIText;
import game.GameWorld;
import game.entities.Player;

public class ShopScreen extends Screen {
	
	private final UIRect	bkgrd;
	private final UIText	title;
	private final UIText	crystalText;
	private final UIButton	backButton;
	private final UIButton	btn1;
	private final UIButton	btn2;
	private final UIButton	btn3;
	private final UIButton	btn4;
	private final UIButton	btn5;
	private final UIButton	btn6;
	private Player			p;
	
	public ShopScreen(Application a) {
		super(a);
		bkgrd = new UIRect(Vec2f.ZERO, Vec2f.ZERO, Color.black, new BasicStroke(0f));
		backButton = new UIButton("Return to Game", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET.darker(),
				Color.white, new BasicStroke(0f));
		btn1 = new UIButton("Jump", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET, GameWorld.DARK_LAVENDER,
				new BasicStroke(0f));
		btn2 = new UIButton("Aura", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET, GameWorld.DARK_LAVENDER,
				new BasicStroke(0f));
		btn3 = new UIButton("Light Loss", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET, GameWorld.DARK_LAVENDER,
				new BasicStroke(0f));
		btn4 = new UIButton("Laser", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET, GameWorld.DARK_LAVENDER,
				new BasicStroke(0f));
		btn5 = new UIButton("Damage", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET, GameWorld.DARK_LAVENDER,
				new BasicStroke(0f));
		btn6 = new UIButton("???", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET, GameWorld.DARK_LAVENDER,
				new BasicStroke(0f));
		title = new UIText("Upgrades", Color.white, Vec2f.ZERO, 1);
		crystalText = new UIText("Crystals: 0", Color.white, Vec2f.ZERO, 1);
	}
	
	protected void setPlayer(Player p) {
		this.p = p;
	}
	
	@Override
	protected void onTick(long nanosSincePreviousTick) {
		crystalText.updateText("Crystals: " + ((p == null) ? 0 : p.getCrystals()));
	}
	
	@Override
	protected void onDraw(Graphics2D g) {
		bkgrd.drawAndFillShape(g);
		title.drawShape(g);
		backButton.drawShape(g);
		btn1.drawShape(g);
		btn2.drawShape(g);
		btn3.drawShape(g);
		btn4.drawShape(g);
		btn5.drawShape(g);
		btn6.drawShape(g);
		crystalText.drawShape(g);
	}
	
	@Override
	protected void onResize(Vec2i newSize) {
		float w = newSize.x;
		float h = newSize.y;
		bkgrd.updatePosition(Vec2f.ZERO, new Vec2f(w, h));
		backButton.updatePosition(new Vec2f(w / 10, 66 * (h / 80)), new Vec2f(4 * w / 7, 76 * (h / 80)));
		float btnWidth = w / 4;
		float btnHeight = h / 8;
		float btnX = w / 10;
		float btnY = h / 4;
		float pad = w / 50;
		btn1.updatePosition(new Vec2f(btnX, btnY), new Vec2f(btnX + btnWidth, btnY + btnHeight));
		btn2.updatePosition(new Vec2f(btnX + btnWidth + pad, btnY), new Vec2f(btnX + 2 * btnWidth + pad, btnY
				+ btnHeight));
		btn3.updatePosition(new Vec2f(btnX + 2 * btnWidth + 2 * pad, btnY), new Vec2f(btnX + 3 * btnWidth + 2 * pad,
				btnY + btnHeight));
		btnY += btnHeight + h / 10;
		btn4.updatePosition(new Vec2f(btnX, btnY), new Vec2f(btnX + btnWidth, btnY + btnHeight));
		btn5.updatePosition(new Vec2f(btnX + btnWidth + pad, btnY), new Vec2f(btnX + 2 * btnWidth + pad, btnY
				+ btnHeight));
		btn6.updatePosition(new Vec2f(btnX + 2 * btnWidth + 2 * pad, btnY), new Vec2f(btnX + 3 * btnWidth + 2 * pad,
				btnY + btnHeight));
		title.resizeText(new Vec2f(w / 10, h / 7), h / 7);
		crystalText.resizeText(new Vec2f(w - w / 3 + w / 60, h / 14), 2 * h / 30);
	}
	
	/**
	 * Checks what was clicked
	 */
	@Override
	protected void onMouseReleased(MouseEvent e) {
		if(backButton.hitTarget(e)) {
			a.popScreen();
		} else if(btn1.hitTarget(e)) {
			
		} else if(btn2.hitTarget(e)) {
			
		} else if(btn3.hitTarget(e)) {
			
		} else if(btn4.hitTarget(e)) {
			
		} else if(btn5.hitTarget(e)) {
			
		} else if(btn6.hitTarget(e)) {
			
		}
	}
	
}
