package engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import engine.entity.Entity;

public class Saver {
	
	/**
	 * Serializes game world to save the game to the passed in string
	 * 
	 * @param fileName
	 *            the file to save to
	 */
	public static void saveGame(String fileName, World game) {
		try {
			FileOutputStream fileOut = new FileOutputStream(new File(fileName));
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(game);
			out.close();
			fileOut.close();
			System.out.println("Game data saved in " + fileName);
		} catch (IOException i) {
			System.err.println("Game couldn't be saved - see stack trace");
			i.printStackTrace();
		}
	}
	
	/**
	 * Loads game world from file and makes the game that
	 * 
	 * @param fileName
	 *            the file to load game from
	 */
	public static World loadGame(String fileName, Viewport v, World w) {
		for (Entity e : w.getEntities()) {
			e.stopSound();
		}
		World tempGame = null;
		try {
			FileInputStream fileIn = new FileInputStream(new File(fileName));
			ObjectInputStream in = new ObjectInputStream(fileIn);
			tempGame = (World) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			System.err.println("I/O issue in loading game: ");
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.err.println("World class not found");
			c.printStackTrace();
			return null;
		}
		if (tempGame != null) {
			tempGame.v = v;
			tempGame.reload();
			System.out.println("Game data loaded from " + fileName);
			return tempGame;
		}
		return null;
	}
	
}
