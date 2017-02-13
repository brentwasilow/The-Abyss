package main;

import level.Level;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.*;

import player.Player;
import render.RenderBackground;
import render.RenderDeath;
import render.RenderEnd;
import render.RenderEnemies;
import render.RenderHUD;
import render.RenderIntro;
import render.RenderMap;
import render.RenderWeapon;
import update.UpdateMovement;
import static org.lwjgl.opengl.GL11.*;

public class Main
{
	public static Level level = Level.level1;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 675;
	public static final int DISTANCE_TO_PROJECTION = (int) (WIDTH / 2.0 / Math.tan(Math.toRadians(Player.FOV / 2.0)));
	public static double ANGLE_BETWEEN_RAYS = ((float) Player.FOV) / WIDTH;
	public static boolean intro = true;

	public static void main(String[] args)
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("The Abyss");
			Display.create();
		} catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		while (!Display.isCloseRequested())
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			if (RenderEnd.end) {
				RenderEnd.render();
			} else {

			if (Player.health > 0)
			{
				if (intro)
				{
					RenderIntro.render();
				} else if (!intro)
				{
					glDisable(GL_TEXTURE_2D);

					RenderBackground.render();
					RenderMap.render();
					RenderEnemies.render();
					RenderWeapon.render();
					RenderHUD.render();
					UpdateMovement.update();
				}
			} else
			{
				RenderDeath.render();
			}
			}

			Display.update();
			Display.sync(60);
		}

		Display.destroy();
		AL.destroy();
	}
}
