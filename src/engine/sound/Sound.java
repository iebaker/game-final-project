package engine.sound;

import java.io.*;

import javax.sound.sampled.*;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * Load and play a sound.
 * Provides functions to play, loop, pause, resume, setVolume, stop.
 * Creates a Clip from an AudioInputStream, which can be from a file,
 * URL or InputStream.
 * Largely taken from an open-source sound management system from potatoland.org
 * 
 * @author smt3
 * 
 */
public class Sound implements Runnable
{
    private AudioInputStream audiostream;
    private Clip clip;
    private URL completeURL;
    private String soundFilename;
    private boolean looping = false;
    private boolean doLoop = false;
    private long pausePosition = 0L;
    private FloatControl gainControl;  // for volume


    /**
     * Load Sound from file in current directory
     */
    public Sound(String audioFilename) {
        audiostream = openAudioStream(null,null,audioFilename);
        clip = makeClip(audiostream);
    }

    /**
     * Load Sound from file in specified base URL
     */
    public Sound(URL codebaseURL, String audioFilename) {
        audiostream = openAudioStream(null,codebaseURL,audioFilename);
        clip = makeClip(audiostream);
    }

    /**
     * Load Sound from a complete URL
     */
    public Sound(URL audioFileFullURL) {
        audiostream = openAudioStream(audioFileFullURL,null,null);
        clip = makeClip(audiostream);
    }

    /**
     * Open the audio stream using the specified URL
     */
    public AudioInputStream openAudioStream(URL fullURL, URL codebaseURL, String audioFilename) {
        // If no complete URL was provided, build a URL to sound file
        if (fullURL == null) {
            // if no codebase given, get from system
            if (codebaseURL == null) {
                try {
					codebaseURL = new URL("file:" + System.getProperty("user.dir") + "/");
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
            }
            // make the complete url from base url and filename
            try {
                fullURL = new URL(codebaseURL, audioFilename);
            } catch (MalformedURLException e){
                System.err.println("Sound.openAudioStream(): URL error: " + e.getMessage());
            }
        }
        // Hold onto paths for reference
        this.completeURL = fullURL;
        this.soundFilename = audioFilename;

        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(completeURL);
        } catch ( UnsupportedAudioFileException e1 ) {
            System.out.println("Sound.openAudioStream(): Audio format not supported.  " + e1);
        } catch(Exception e) {
            System.out.println("Sound.openAudioStream(): Exception when loading sound from URL: " + completeURL);
            System.out.println(e);
            return null;
        }
        return audioIn;
    }

    /**
     * Open the audio stream using an InputStream
     */
    public AudioInputStream openAudioStream(InputStream in)
    {
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(in,1024));
        } catch ( UnsupportedAudioFileException e1 ) {
            System.out.println("Sound.openAudioStream(): Audio format not supported.  " + e1);
        } catch(Exception e) {
            System.out.println("Exception when loading sound from inputStream: " + e);
            return null;
        }
        return audioIn;
    }

    /**
     * Make the Clip from the audio stream, create volume control
     */
    public Clip makeClip(AudioInputStream audioIn)
    {
        Clip aClip = null;
        try {
            //System.out.println(audiosource.getFormat());
            DataLine.Info dataLineInfo = new DataLine.Info( Clip.class, audioIn.getFormat() );
            aClip = (Clip) AudioSystem.getLine(dataLineInfo);
            aClip.open( audioIn );
            // For volume control
            //gainControl = (FloatControl) aClip.getControl(FloatControl.Type.VOLUME);
            gainControl = (FloatControl) aClip.getControl(FloatControl.Type.MASTER_GAIN);
            //muteControl = (BooleanControl)clip.getControl(BooleanControl.Type.MUTE);
        } catch ( LineUnavailableException e2 ) {
            e2.printStackTrace();
        } catch ( IOException e3 ) {
            e3.printStackTrace();
        }
        return aClip;
    }

    /**
     * If true, play() will loop sound continuously
     */
    public void setLoop(boolean loopflag) {
        doLoop = loopflag;
    }

    /**
     * Play sound from beginning
     */
    public void play() {
        play(0);
    }

    /**
     * Play sound from beginning and loop
     */
    public void loop() {
        doLoop = true;
        play(0);
    }

    /**
     *  is the clip currently running?
     */
    public boolean isPlaying() {
        return clip.isRunning();
    }

    /**
     * if arg is true, stop the clip
     * if arg is false, restart the clip at the last pause position
     */
    public void pause(boolean stop) {
        if (stop) {
            pausePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
        else {
            play(pausePosition);
        }
    }

    /**
     * Stop the clip
     */
    public void stop() {
        clip.stop();    //Stop the sound
        looping = false;
    }

    /**
     * Play the clip starting at playPosition.  If setLoop(true) or loop() were
     * called, clip will loop continuously.
     */
    public void play(long playPosition) {   // millisecond start position
        if (clip == null) {
            System.out.println("Sound " + soundFilename + " not loaded yet.");
        }
        else if (looping) {
            System.out.println("already looping sound " + soundFilename);
        }
        else if (doLoop) {
            System.out.println("Playing sound " + soundFilename + " continuously.");
            clip.loop( Clip.LOOP_CONTINUOUSLY );
            looping = true;
        }
        else {
            System.out.println("Play sound " + soundFilename + " once.");
            clip.setMicrosecondPosition( playPosition );
            clip.loop(0);
        }
    }

    /**
     * Set the volume to a value between 0 and 1.
     */
    public void setVolume(double value) {
        // value is between 0 and 1
        value = (value<=0.0)? 0.0001 : ((value>1.0)? 1.0 : value);
        try {
            float dB = (float)(Math.log(value)/Math.log(10.0)*20.0);
            gainControl.setValue(dB);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Fade the volume to a new value.  To shift volume while sound is playing,
     * ie. to simulate motion to or from an object, the volume has to change
     * smoothly in a short period of time.  Unfortunately this makes an annoying
     * clicking noise, mostly noticeable in the browser.  I reduce the click
     * by fading the volume in small increments with delays in between.  This
     * means that you can't change the volume very quickly.  The fade has to
     * to take a second or two to prevent clicks.
     */
    float currDB = 0F;
    float targetDB = 0F;
    float fadePerStep = 1;   // .1 works for applets, 1 is okay for apps
    boolean fading = false;

    public void shiftVolumeTo(double value) {
        // value is between 0 and 1
        value = (value<=0.0)? 0.0001 : ((value>1.0)? 1.0 : value);
        targetDB = (float)(Math.log(value)/Math.log(10.0)*20.0);
        if (!fading) {
            Thread t = new Thread(this);  // start a thread to fade volume
            t.start();  // calls run() below
        }
    }

    /**
     * Run by thread, this will step the volume up or down to a target level.
     * Applets need fadePerStep=.1 to minimize clicks.
     * Apps can get away with fadePerStep=1.0 for a faster fade with no clicks.
     */
    public void run()
    {
        fading = true;   // prevent running twice on same sound
        if (currDB > targetDB) {
            while (currDB > targetDB) {
                currDB -= fadePerStep;
                gainControl.setValue(currDB);
                try {Thread.sleep(10);} catch (Exception e) {}
            }
        }
        else if (currDB < targetDB) {
            while (currDB < targetDB) {
                currDB += fadePerStep;
                gainControl.setValue(currDB);
                try {Thread.sleep(10);} catch (Exception e) {}
            }
        }
        fading = false;
        currDB = targetDB;  // now sound is at this volume level
    }
}