package smt3.gameengine.other;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

//This way of reading XMLs is taken from an article by Mohamed Sanualla that can be found here:
//http://www.javacodegeeks.com/2013/05/parsing-xml-using-dom-sax-and-stax-parser-in-java.html#stax

/*BufferedImage img = null;
try {
    img = ImageIO.read(new File("strawberry.jpg"));
} catch (IOException e) {
}*/
public class Resource {
	
	private Hashtable<String, Sprite> _spriteHash;

	public Resource(String toRead, ArrayList<String> images) {
		// TODO Auto-generated constructor stub
		Hashtable<String, Image> sheetHash = new Hashtable<String, Image>();
		for(String imgSrc : images) {
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File(imgSrc));
			}
			catch (IOException e) {
				System.out.println("Could not read spritesheet");
				e.printStackTrace();
			}
			sheetHash.put(imgSrc, img);
		}
		//Where the spritesheet is stored
		String tagContent = null;
		Sprite currSprite = null;
		String currSpriteID = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		//Hashmap that stores links to Sprites given their IDs
		_spriteHash = new Hashtable<String, Sprite>();
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(toRead, new FileInputStream(toRead));
			
			while(reader.hasNext()) {
				int event = reader.next();
				
				switch(event) {
				case XMLStreamConstants.START_ELEMENT:
					if("character".equals(reader.getLocalName()) || "tile".equals(reader.getLocalName()) || "extra".equals(reader.getLocalName()) || "enemy".equals(reader.getLocalName())) {
						currSpriteID = reader.getAttributeValue(0);
						currSprite = new Sprite();
					}
					if("mode".equals(reader.getLocalName())) {
						currSprite.setMode(reader.getAttributeValue(0));
					}
					if("frame".equals(reader.getLocalName())) {
						currSprite.setFrame(Integer.parseInt(reader.getAttributeValue(0)));
					}
					break;
				
				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					break;
				
				case XMLStreamConstants.END_ELEMENT:
					String name = reader.getLocalName();
					if(name == "type") {
						currSprite.setType(tagContent);
					}
					else if(name == "spriteW") {
						currSprite.setW(Integer.parseInt(tagContent));
					}
					else if(name == "spriteH") {
						currSprite.setH(Integer.parseInt(tagContent));
					}
					else if(name == "spriteX") {
						currSprite.setX(Integer.parseInt(tagContent));
					}
					else if(name == "spriteY") {
						currSprite.setY(Integer.parseInt(tagContent));
					}
					else if(name == "character" || name == "tile" || name == "extra" || name == "enemy") {
						_spriteHash.put(currSpriteID, currSprite);
					}
					else if(name == "img") {
						currSprite.setSrc(sheetHash.get(tagContent));
					}
					else if(name == "frames") {
						currSprite.setFrameHash(Integer.parseInt(tagContent));
					}
					else if(name == "static") {
						if("true".equals(tagContent)) {
							currSprite.setStatic(true);
						}
						else if("false".equals(tagContent)){
							currSprite.setStatic(false);
						}
						else {
							System.out.println("Warning: sprite of type " + currSprite.getType() + " is neither static nor animated");
						}
					}
				}
			}
		
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not read XML");
			e.printStackTrace();
		}
		
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not locate file");
			e.printStackTrace();
		}
	}
	
	public Hashtable<String, Sprite> getSpriteHash() {
		return _spriteHash;
	}
	
	public Sprite getSprite(int ID) {
		return _spriteHash.get(ID);
	}
}