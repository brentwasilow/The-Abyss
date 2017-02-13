package render;

import static org.lwjgl.opengl.GL11.*;
import main.Main;

/**
 * A static class that renders the background to
 * the screen using the render function.
 * 
 * @author btwasilow
 *
 */

public final class RenderBackground
{	
	public final static void render()
	{
		for (int y = 0; y < Main.HEIGHT; y++) 
		{
			if (y > Main.HEIGHT>>1) glColor3f(0.0f, 0.0f, 0.0f);
			else glColor3f(0.0f, 0.0f, 0.0f);
		
			glBegin(GL_LINES);
				glVertex2f(0, y);
				glVertex2f(Main.WIDTH, y);
			glEnd();
		}
	}
}
