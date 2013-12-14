package game.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import cs195n.Vec2f;
import cs195n.Vec2i;
import engine.Application;
import engine.Saver;
import engine.Screen;
import engine.ui.TextBox;
import engine.ui.UIButton;
import engine.ui.UIRect;
import engine.ui.UIText;
import game.GameWorld;
import game.entities.Player;

public class ShopScreen extends Screen {
	
	private final UIRect		bkgrd;
	private final UIText		title;
	private final UIText		crystalText;
	private final UIButton		backButton;
	public static ShopButton[]	buttons;
	private Player				player;
	private TextBox				textBox;
	
	public ShopScreen(Application a) {
		super(a);
		bkgrd = new UIRect(Vec2f.ZERO, Vec2f.ZERO, Color.black, new BasicStroke(0f));
		backButton = new UIButton("Return to Game", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET.darker(),
				Color.white, null, new BasicStroke(0f));
		title = new UIText("Upgrades", Color.white, Vec2f.ZERO, 1);
		crystalText = new UIText("Crystals: 0", Color.white, Vec2f.ZERO, 1);
	}
	
	protected void setWorld(GameWorld world) {
		player = (Player) world.getPlayer();
		Saver.saveGame(GameWorld.SAVEFILE, world);
		for (ShopButton btn : ShopScreen.buttons) {
			if (player.getCrystals() < btn.requiredCrystals())
				btn.disable(false);
			else if (!btn.isPurchased()) btn.enable();
		}
	}
	
	@Override
	protected void onTick(long nanosSincePreviousTick) {
		crystalText.updateText("Crystals: " + (player.getCrystals()));
	}
	
	@Override
	protected void onDraw(Graphics2D g) {
		bkgrd.drawAndFillShape(g);
		title.drawShape(g);
		backButton.drawShape(g);
		for (ShopButton btn : ShopScreen.buttons) {
			btn.drawShape(g);
		}
		crystalText.drawShape(g);
		if (textBox != null) {
			textBox.onDraw(g);
		}
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
		for (int i = 0; i < ShopScreen.buttons.length; i++) {
			float btnXStart = btnX;
			float btnXEnd = btnX + btnWidth;
			ShopScreen.buttons[i].updatePosition(new Vec2f(btnXStart, btnY), new Vec2f(btnXEnd, btnY + btnHeight));
			btnX += btnWidth + pad;
			if (i == 2) {
				btnY += btnHeight + h / 10;
				btnX = w / 10;
			}
		}
		title.resizeText(new Vec2f(w / 10, h / 7), h / 7);
		crystalText.resizeText(new Vec2f(w - w / 3 + w / 60, h / 14), 2 * h / 30);
	}
	
	/**
	 * Checks what was clicked
	 */
	@Override
	protected void onMouseReleased(MouseEvent e) {
		if (backButton.hitTarget(e)) {
			a.popScreen();
		}
		for (int i = 0; i < ShopScreen.buttons.length; i++) {
			if (ShopScreen.buttons[i].hitTarget(e)) {
				if (i == 0 && !ShopScreen.buttons[i].isDisabled()
						&& player.spendCrystals(ShopScreen.buttons[i].requiredCrystals())) {
					player.unlockHighJump();
					ShopScreen.buttons[i].setPurchased();
				}
			}
		}
	}
}
