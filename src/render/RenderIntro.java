package render;

import java.awt.Font;
import java.io.InputStream;

import main.Main;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class RenderIntro
{
	public static boolean initialize = true;
	public static TrueTypeFont font;

	public static void render()
	{
		if (initialize)
		{
			init();
		} else
		{
			Color.white.bind();
			
			font.drawString(50, 50, "THE ABYSS", Color.white);
			font.drawString(50, 100, "PRESS ENTER TO BEGIN...", Color.white);
			
			if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				Main.intro = false;
				Main.level.song.playAsMusic(1.0f, 1.0f, true);
			}
		}
	}

	private static void init()
	{
		try
		{
			InputStream inputStream = ResourceLoader.getResourceAsStream("fonts/font3.ttf");

			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(32f); // set font size
			font = new TrueTypeFont(awtFont2, true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		initialize = false;
	}
}
