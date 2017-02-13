package render;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.Color;

import player.Player;
import texture.Textures;

public class RenderHUD
{
	public static void render()
	{
		Textures.t.sprites.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glColor3f(255, 0, 0);
		
		float imageSize = 512.0f;
		
		float texX;
		float texX_1;
		float texY;
		float texY_16;
		
		if (Player.health >= 100) {
			texX = 64/imageSize;
			texX_1 = 80/imageSize;
			texY = 0/imageSize;
			texY_16 = 16/imageSize;
		} else if (Player.health > 80) {
			texX = 80/imageSize;
			texX_1 = 96/imageSize;
			texY = 0/imageSize;
			texY_16 = 16/imageSize;
		} else if (Player.health > 60) {
			texX = 96/imageSize;
			texX_1 = 112/imageSize;
			texY = 0/imageSize;
			texY_16 = 16/imageSize;
		} else if (Player.health > 40) {
			texX = 112/imageSize;
			texX_1 = 128/imageSize;
			texY = 0/imageSize;
			texY_16 = 16/imageSize;
		} else if (Player.health > 20) {
			texX = 64/imageSize;
			texX_1 = 80/imageSize;
			texY = 16/imageSize;
			texY_16 = 32/imageSize;
		} else {
			texX = 80/imageSize;
			texX_1 = 96/imageSize;
			texY = 16/imageSize;
			texY_16 = 32/imageSize;
		}
		
		glBegin(GL_QUADS);
			glTexCoord2f(texX, texY_16);
			glVertex2f(16, 48);
			glTexCoord2f(texX, texY);
			glVertex2f(16, 16);
			glTexCoord2f(texX_1, texY);
			glVertex2f(48, 16);
		    glTexCoord2f(texX_1, texY_16);
			glVertex2f(48, 48);
		glEnd();
		
		if (Player.key) {
			glBegin(GL_QUADS);
				glColor3f(1.0f, 1.0f, 0);
				glTexCoord2f(16/imageSize, 67/imageSize);
				glVertex2f(8, 158); //158
				glTexCoord2f(16/imageSize, 35/imageSize);
				glVertex2f(8, 110); //110
				glTexCoord2f(48/imageSize, 35/imageSize);
				glVertex2f(56, 110);
				glTexCoord2f(48/imageSize, 67/imageSize);
				glVertex2f(56, 158);
			glEnd();
		}
		
		if (Player.potion != 0) {
			glBegin(GL_QUADS);
				glColor3f(0.0f, 1.0f, 1.0f);
				glTexCoord2f(144/imageSize, 70/imageSize);
				glVertex2f(8, 104); //104
				glTexCoord2f(144/imageSize, 38/imageSize);
				glVertex2f(8, 56); //56
				glTexCoord2f(176/imageSize, 38/imageSize);
				glVertex2f(56, 56);
				glTexCoord2f(176/imageSize, 70/imageSize);
				glVertex2f(56, 104);
			glEnd();
			
			RenderIntro.font.drawString(60, 66, "x" + Player.potion, Color.white);
		}
		
		RenderIntro.font.drawString(450, 16, "EXP:" + Player.xp, Color.white);
	}
}
