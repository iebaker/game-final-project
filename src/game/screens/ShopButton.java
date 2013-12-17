package game.screens;

import java.awt.BasicStroke;

import cs195n.Vec2f;
import engine.ui.UIButton;
import game.GameWorld;

public class ShopButton extends UIButton {
	
	private static final long	serialVersionUID	= 6740786490718563693L;
	private final int			crystalCount;
	
	public ShopButton(String msg, int crystalCount) {
		super(msg + " (" + crystalCount + ")", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET, GameWorld.DARK_LAVENDER,
				GameWorld.DUSKY_VIOLET.darker().darker(), new BasicStroke(0f));
		this.crystalCount = crystalCount;
	}
	
	/**
	 * Returns the number of crystals required to purchase
	 * 
	 * @return the required number of crystals
	 */
	public int requiredCrystals() {
		return crystalCount;
	}
	
	public void setPurchased() {
		disable(true);
	}
	
	public boolean isPurchased() {
		return permanent;
	}
	
}
