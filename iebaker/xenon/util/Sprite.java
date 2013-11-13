package iebaker.xenon.util;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite {
	private SpriteGroup parent_group;
	private java.util.List<Frame> my_frames = new ArrayList<Frame>();

	public class Frame {
		public BufferedImage img;

		public void drawSelf(Graphics2D g, int dx1, int dy1, int dx2, int dy2) {
			g.drawImage(img, dx1, dy1, dx2, dy2, null);
		}

		public BufferedImage getImage() {
			return img;
		}
	}

	public Frame frame(int i) {
		return my_frames.get(i);
	}

	public void addFrame(int index, BufferedImage img) {
		Frame f = new Frame();
		f.img = img;
		my_frames.add(index, f);
	}

	public void setParentGroup(SpriteGroup sg) {
		parent_group = sg;
	}

	public int frameCount() {
		return my_frames.size();
	}
}