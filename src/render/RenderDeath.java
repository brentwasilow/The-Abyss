package render;

import main.Main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import audio.Effects;

public class RenderDeath
{
	public static boolean death = true;
	
	public static void render() {
	
		if (death) {
			Main.level.song.stop();
			Effects.e.gameOver.playAsSoundEffect(1.0f, 1.0f, false);
			death = false;
		}
		RenderIntro.font.drawString(50, 50, "GAME OVER...", Color.white);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			Display.destroy();
			AL.destroy();
			System.exit(0);
		}
	}
}
