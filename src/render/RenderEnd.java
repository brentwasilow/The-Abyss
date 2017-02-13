package render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import audio.Effects;

public class RenderEnd
{
	public static boolean end = false;
	public static boolean sound = true;
	
	public static void render() {
		
		if (sound) {
			Effects.e.pickup.playAsSoundEffect(1.0f, 1.0f, false);
			sound = false;
		}
		RenderIntro.font.drawString(50, 50, "YOU FINISHED THE GAME...", Color.white);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			Display.destroy();
			AL.destroy();
			System.exit(0);
		}
	}
}
