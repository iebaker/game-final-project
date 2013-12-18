package iebaker.krypton.world;

import java.util.HashMap;
import java.awt.image.BufferedImage;

public class SpriteGroup {
	private java.util.Map<String, Sprite> my_sprites = new HashMap<String, Sprite>();
	private BufferedImage my_image;

	public Sprite sprite(String sprite_name) {
		return my_sprites.get(sprite_name);
	}

	public void setImage(BufferedImage i) {
		my_image = i;
	}

	public void addSprite(String sprite_name, Sprite s) {
		my_sprites.put(sprite_name, s);
	}

	public BufferedImage getImage() {
		return my_image;
	}
}