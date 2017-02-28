package geozombies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunListener;
import repast.simphony.parameter.Parameters;

/**
 * Play Sound effects for the GeoZombies demo.
 * 
 * @author Eric Tatara
 *
 */
public class SoundEffects implements RunListener {

	public static String SCREAM_FILE = "data/scream.wav";
	public static String ZOMBIE_MOAN_FILE = "data/zombiemoan.wav";
	public static String ENRAGED_ZOMBIES_FILE = "data/enragedzombies.wav";
	
	protected Clip zombieMoanClip;
	protected Clip screamClip;
	protected Clip enragedZombiesClip;
	
	protected List<Clip> clipList;
	
	public static SoundEffects instance;
	
	public static void init(){
		instance = new SoundEffects();
	}
	
	public SoundEffects(){
		RunEnvironment.getInstance().addRunListener(this);
		clipList = new ArrayList<Clip>();
		
		// Load sound clips from files.
		try{
			File file = new File(SCREAM_FILE);
			screamClip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			screamClip.open(ais);
			clipList.add(screamClip);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		try{
			File file = new File(ZOMBIE_MOAN_FILE);
			zombieMoanClip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			zombieMoanClip.open(ais);
			clipList.add(zombieMoanClip);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		try{
			File file = new File(ENRAGED_ZOMBIES_FILE);
			enragedZombiesClip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			enragedZombiesClip.open(ais);
			clipList.add(enragedZombiesClip);
		}
		catch (Exception e){
			e.printStackTrace();
		}	
	}

	
	public  void playScream(){
		Parameters params = RunEnvironment.getInstance().getParameters();
		Boolean play = (Boolean)params.getBoolean("playSounds");
		
		// don't play multiple at same time or when play disabled
		if (!play || screamClip.isActive()) return;  
		
		screamClip.setFramePosition(0); // rewind
		screamClip.start();
	}
	
	public  void playZombieMoan(){
		Parameters params = RunEnvironment.getInstance().getParameters();
		Boolean play = (Boolean)params.getBoolean("playSounds");
		
		// don't play multiple at same time or when play disabled
		if (!play || zombieMoanClip.isActive()) return;  
		
		zombieMoanClip.setFramePosition(0); // rewind
		zombieMoanClip.start();
	}
	
	public  void playEnragedZombies(){
		Parameters params = RunEnvironment.getInstance().getParameters();
		Boolean play = (Boolean)params.getBoolean("playSounds");
		
		// don't play multiple at same time or when play disabled
		if (!play || enragedZombiesClip.isActive()) return; 
		
		enragedZombiesClip.setFramePosition(0); // rewind
		enragedZombiesClip.start();
	}
	
	
	/**
	 * RunListener methods below stops any playing sounds when sim is paused or stopped.
	 */
	
	@Override
	public void stopped() {
		for (Clip clip : clipList){
			clip.stop();
		}
	}

	@Override
	public void paused() {
		for (Clip clip : clipList){
			clip.stop();
		}
	}

	@Override
	public void started() {
	}

	@Override
	public void restarted() {
		for (Clip clip : clipList){
			clip.stop();
		}
	}

	public static SoundEffects getInstance() {
		if (instance == null){
			init();
		}
		
		return instance;
	}
}