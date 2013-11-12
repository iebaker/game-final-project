package engine.sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * A setup to make sound management a little bit easier.
 * Should be instantiated when the game starts, and all of the sound files should be added to the hashtable
 * 
 * @author smt3
 *
 */
public class SoundHolder {

	public static final Hashtable<String, Sound> sounds = new Hashtable<String, Sound>();
	
	public SoundHolder(String toRead) {
		String tagContent = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(toRead, new FileInputStream(toRead));
			String currSoundID = null;
			while(reader.hasNext()) {
				int event = reader.next();				
				switch(event) {
				case XMLStreamConstants.START_ELEMENT:
					if("sound".equals(reader.getLocalName())) {
						currSoundID = reader.getAttributeValue(0);
					}
					break;
				
				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					break;
				
				case XMLStreamConstants.END_ELEMENT:
					String name = reader.getLocalName();
					if(name == "file") {
						sounds.put(currSoundID, new Sound(tagContent));
					}
				}
			}
		
		} catch (XMLStreamException e) {
			System.out.println("Could not read XML");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Could not locate file");
			e.printStackTrace();
		}
	}
}
