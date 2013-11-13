package iebaker.xenon.util;

import java.util.HashMap;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.namespace.QName;
import java.awt.Image;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileReader;

public class Sprites {
	private static java.util.Map<String, SpriteGroup> my_sprite_groups = new HashMap<String, SpriteGroup>();
	private static SpriteGroup current_sprite_group = new SpriteGroup();
	private static String current_spg_name = null;
	private static Sprite current_sprite = new Sprite();
	private static String current_sprite_name = null;
	private static XMLInputFactory factory = null;
	private static XMLStreamReader reader = null;

	public static void init(String xml_filename) throws XMLStreamException {
		factory = XMLInputFactory.newInstance();

		try {
			FileReader fr = new FileReader("lib/sprites.xml");
			reader = factory.createXMLStreamReader(fr);
		} catch (Exception e) {
			System.err.println("Fuck");
			e.printStackTrace();
		}

		if(reader != null) {
			while(reader.hasNext()) {
				int reader_event = reader.next();

				switch(reader_event) {
					case XMLStreamConstants.START_ELEMENT:
						if("group".equals(reader.getLocalName())) {
							initSpriteGroup();
						}

						if("sprite".equals(reader.getLocalName())) {
							initSprite();
						}

						if("frame".equals(reader.getLocalName())) {
							handleFrame();
						}
						break;

					case XMLStreamConstants.END_ELEMENT:
						if("group".equals(reader.getLocalName())) {
							my_sprite_groups.put(current_spg_name, current_sprite_group);
						}

						if("sprite".equals(reader.getLocalName())) {
							current_sprite_group.addSprite(current_sprite_name, current_sprite);
						}
						break;

					case XMLStreamConstants.START_DOCUMENT:
						my_sprite_groups = new HashMap<String, SpriteGroup>();
						break;
				}
			}
		} else {
			System.out.println("The reader was null");
		}
	}

	public static SpriteGroup group(String group_name) {
		return my_sprite_groups.get(group_name);
	}

	private static void initSpriteGroup() {
		current_sprite_group = new SpriteGroup();
		for(int i = 0; i < reader.getAttributeCount(); ++i) {
			QName name = reader.getAttributeName(i);
			if("name".equalsIgnoreCase(name.toString())) {
				current_spg_name = reader.getAttributeValue(i);
			}
			if("src".equalsIgnoreCase(name.toString())) {
				BufferedImage img = null;
				try {
					img = ImageIO.read(new File("lib/" + reader.getAttributeValue(i)));
					current_sprite_group.setImage(img);
				} catch (IOException e) {
					System.out.println("Warning!");
				}
			}
		}
	}

	private static void initSprite() {
		current_sprite = new Sprite();
		current_sprite.setParentGroup(current_sprite_group);
		for(int i = 0; i < reader.getAttributeCount(); ++i) {
			QName name = reader.getAttributeName(i);
			if("name".equalsIgnoreCase(name.toString())) {
				current_sprite_name = reader.getAttributeValue(i);
			}
		}
	}

	private static void handleFrame() {
		int index = -1;
		int x1 = -1;
		int y1 = -1;
		int w = -1;
		int h = -1;

		for(int i = 0; i < reader.getAttributeCount(); ++i) {
			QName name = reader.getAttributeName(i);
			if("index".equalsIgnoreCase(name.toString())) {
				index = Integer.parseInt(reader.getAttributeValue(i));
			}
			if("region".equalsIgnoreCase(name.toString())) {
				String[] reg = reader.getAttributeValue(i).split(",");
				if(reg.length >= 4) {
					x1 = Integer.parseInt(reg[0]);
					y1 = Integer.parseInt(reg[1]);
					w = Integer.parseInt(reg[2]);
					h = Integer.parseInt(reg[3]);
				}
			}
		}

		if(index != -1 && x1 != -1 && w != -1 && y1 != -1 && h != -1) {
			BufferedImage bi = current_sprite_group.getImage().getSubimage(x1, y1, w, h);
			current_sprite.addFrame(index, bi);
		}
	}
}