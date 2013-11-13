package engine.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Load and plays a longer music file than a Sound. Good for background music.
 * Volume is constant, but starting and stopping the thread can pause and resume playback.
 * Does not currently support looping sound, but I'm working on it.
 * 
 * @author Sawyer
 *
 */
public class MusicPlayer extends Thread {
	
	private String _file;
	private volatile boolean _soundPaused = false;
	
	public MusicPlayer(String file) {
		_file = file;
	}
	
	public void run() {
		SourceDataLine soundLine = null;
	     int BUFFER_SIZE = 64*1024;  // 64 KB

	      // Set up an audio input stream piped from the sound file.
	      try {
	         File soundFile = new File(_file);
	         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
	         AudioFormat audioFormat = audioInputStream.getFormat();
	         DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
	         soundLine = (SourceDataLine) AudioSystem.getLine(info);
	         soundLine.open(audioFormat);
	         soundLine.start();
	         int nBytesRead = 0;
	         byte[] sampledData = new byte[BUFFER_SIZE];
	         while (nBytesRead != -1) {
	        	if(!_soundPaused) {
		            nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
		            if (nBytesRead >= 0) {
		               // Writes audio data to the mixer via this source data line.
		               soundLine.write(sampledData, 0, nBytesRead);
		            }
	        	}
	         }
	      } catch (UnsupportedAudioFileException ex) {
	         ex.printStackTrace();
	      } catch (IOException ex) {
	         ex.printStackTrace();
	      } catch (LineUnavailableException ex) {
	         ex.printStackTrace();
	      } finally {
	         soundLine.drain();
	         soundLine.close();
	      }
	}
}
