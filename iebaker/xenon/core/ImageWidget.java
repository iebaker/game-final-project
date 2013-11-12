package iebaker.xenon.core;

import cs195n.Vec2f;

import iebaker.xenon.slice.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageWidget extends Widget {
	protected BufferedImage my_image;
	protected BufferedImage my_hover_image;

	public ImageWidget(Application a, Screen s, String id) {
		super(a, s, id);
	}

	public ImageWidget(Screen s, String id) {
		super(s, id);
	}

	public void setImage(String image_file_name) {
		try {
			my_image = ImageIO.read(new File(image_file_name));
		} catch(IOException e) {
			System.err.println("[ImageWidget.setImage()] Failure loading image for image widget.");
			e.printStackTrace();
		}
	}

	public void setHoverImage(String image_file_name) {
		try {
			my_hover_image = ImageIO.read(new File(image_file_name));
		} catch(IOException e) {
			System.err.println("[ImageWidget.setHoverImage()] Failure loading image for image widget hover.");
			e.printStackTrace();
		}
	}

	@Override
	public void onDraw(Artist a, Graphics2D g) {
		BufferedImage image_to_draw;
		if(mouse_over) {
			image_to_draw = (my_hover_image == null) ? my_image : my_hover_image;
		} else {
			image_to_draw = my_image;
		}
		if(image_to_draw != null) a.image(g, image_to_draw, (int)attrLocation.x, (int)attrLocation.y, (int)attrSize.x, (int)attrSize.y);
	}
}