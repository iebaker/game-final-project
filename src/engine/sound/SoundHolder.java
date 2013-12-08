package engine.sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * A setup to make sound management a little bit easier. Should be instantiated when the game starts, and the filename
 * of the XML should be passed in
 * 
 * @author smt3
 * 
 */
public class SoundHolder {
	
	public static final Hashtable<String, Sound>	soundTable	= new Hashtable<String, Sound>();
	
	/**
	 * Consturctor. Takes in the path of an XML to parse
	 * 
	 * @param toRead
	 */
	public SoundHolder(String toRead) {
		SoundHolder.soundTable.clear();
		String tagContent = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(toRead, new FileInputStream(toRead));
			String currSoundID = null;
			while (reader.hasNext()) {
				int event = reader.next();
				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					if ("sound".equals(reader.getLocalName())) {
						currSoundID = reader.getAttributeValue(0);
					}
					break;
				
				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					break;
				
				case XMLStreamConstants.END_ELEMENT:
					String name = reader.getLocalName();
					if (name == "file") {
						SoundHolder.soundTable.put(currSoundID, new Sound(tagContent));
					}
				}
			}
			
		} catch (XMLStreamException e) {
			System.err.println("Sound XML is improperly formatted");
		} catch (FileNotFoundException e) {
			System.err.println("Could not locate sound XML file");
		}
	}
}
