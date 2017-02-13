package audio;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Represents sound effects that will be utilized
 * throughout the game.
 * 
 * @author Brent Thomas Wasilow
 *
 */

public class Effects
{
	public Audio door;
	public Audio fire;
	public Audio breakSound;
	public Audio pickup;
	public Audio click;
	public Audio death;
	public Audio gameOver;
	public Audio hit;

	public static Effects e = new Effects();
	
	public Effects()
	{
		try
		{
			door = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/door.wav"));
			fire = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/fire.wav"));
			breakSound = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/thud1.wav"));
			pickup = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/pickup.wav"));
			click = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/click.wav"));
			death = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/kill.wav"));
			gameOver = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/death.wav"));
			hit = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sounds/hit.wav"));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
