package render;

import static org.lwjgl.opengl.GL11.*;
import java.io.File;
import org.lwjgl.input.Keyboard;
import audio.PCMFilePlayer;
import main.Main;
import sprite.Sprite;
import texture.Textures;

public class RenderWeapon
{
	public static int xOffset = 128;	
	public static boolean begin = false;
	public static int lower = 0;
	public static int timer = 0;
	public static int timer2 = 0;
	
	/**
	 * Renders the weapon to the screen as well as animates it.
	 * If the z key is pressed and the animation hasnt begun, then
	 * start the animation. increase the timer slowly and set the 
	 * texture to the animated texture. If the timer hits 20 then
	 * set all the variables back to their non-animated values until
	 * the z key is pressed again.
	 */
	
	public static void render()
	{
		Textures.t.handTexture.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Z) && begin == false) {
			begin = true;
			timer2 = 0;
			lower = 0;
			//Effects.e.fire.playAsSoundEffect(1.0f, 2.0f, false);
			try
			{
				PCMFilePlayer p = new PCMFilePlayer(new File("res/sounds/fire.wav"));
				p.start();
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Main.level.sprites.add(new Sprite((int)Main.level.x, (int)Main.level.y, 'f'));
			
		}
		
		if (begin) {
			timer++;
			
			if (timer < 20) xOffset = 0;
			else {
				timer = 0;
				xOffset = 128;
				begin = false;
			}
		}
		
		if (!begin) {
			if (timer2 == 30 && lower < 128) {
				lower+=2;
			} else {
				timer2++;
			}
		}
		
		glColor3f(1.0f, 1.0f, 1.0f);
		
		glBegin(GL_QUADS);
			glTexCoord2f((0+xOffset)/256.0f, 128/128.0f);
			glVertex2f(Main.WIDTH/2-128, Main.HEIGHT + lower);
			
			glTexCoord2f((0+xOffset)/256.0f, 0/128.0f);
			glVertex2f(Main.WIDTH/2-128, Main.HEIGHT-256 + lower);
			
			glTexCoord2f((128+xOffset)/256.0f, 0/128.0f);
			glVertex2f(Main.WIDTH/2+128, Main.HEIGHT-256+lower);
			
		    glTexCoord2f((128+xOffset)/256.0f, 128/128.0f);
			glVertex2f(Main.WIDTH/2+128, Main.HEIGHT+lower);
		glEnd();
	}
}
