package engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import engine.entity.Entity;

public class Saver {
	
	private static class SaveGame implements Runnable {
		
		private final String	fileName;
		private final World		w;
		
		public SaveGame(String fileName, World w) {
			this.fileName = fileName;
			this.w = w;
		}
		
		@Override
		public void run() {
			try {
				FileOutputStream fileOut = new FileOutputStream(new File(fileName));
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(w);
				out.close();
				fileOut.close();
			} catch (IOException i) {
				System.err.println("Game couldn't be saved - see stack trace");
				i.printStackTrace();
			}
		}
	}
	
	/**
	 * Serializes game world to save the game to the passed in string
	 * 
	 * @param fileName
	 *            the file to save to
	 */
	public static void saveGame(String fileName, World w) {
		(new Thread(new SaveGame(fileName, w))).start();
	}
	
	/**
	 * Loads game world from fileName
	 * 
	 * @param fileName
	 * @param v
	 * @param w
	 * @return
	 */
	public static World loadGame(String fileName, Viewport v, World w) {
		World tempGame = null;
		try {
			FileInputStream fileIn = new FileInputStream(new File(fileName));
			ObjectInputStream in = new ObjectInputStream(fileIn);
			tempGame = (World) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			System.err.println("I/O issue in loading game: ");
			return null;
		} catch (ClassNotFoundException c) {
			System.err.println("World class not found");
			return null;
		}
		for (Entity e : w.getEntities()) {
			e.stopSound();
		}
		
		for (Entity e : w.getPassableEntities()) {
			e.stopSound();
		}
		
		if (tempGame != null) {
			tempGame.v = v;
			tempGame.reload();
			return tempGame;
		}
		return null;
	}
	
}
