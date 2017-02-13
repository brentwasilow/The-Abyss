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

import java.io.File;

import org.lwjgl.input.Keyboard;

import level.Level;
import main.Main;
import player.Player;
import sprite.Sprite;
import texture.Textures;
import wall.Wall;
import audio.Effects;
import audio.PCMFilePlayer;

public class RenderEnemies
{	
	public static int animation = 0;
	public static int animation2 = 0;
	public static int animation3 = 0;
	public static boolean clickable = true;
	public static boolean changingLevel = false;
	public static int addTime = 0;
	
	public static void render()
	{	
		if (Keyboard.isKeyDown(Keyboard.KEY_X) && clickable) {
			if (Player.health <= 90 && Player.potion != 0) {
				clickable = false;
				Player.health += 20;
				if (Player.health > 100) Player.health = 100;
				Player.potion--;
				Effects.e.pickup.playAsSoundEffect(1.0f, 1.0f, false);
			}
		}
		
		if (Player.xp == 200) {
			Player.attack = 40;
		}
		if (!clickable) {
			addTime++;
		}
		
		if (addTime > 10) {
			clickable = true;
			addTime = 0;
		}
		animation++;
		animation2++;
		
		Textures.t.sprites.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		int size = Main.level.sprites.size();
		
		for (int i = 0; i < size; i++)
		{
			Sprite e = Main.level.sprites.get(i);
			checkEnemy(e);
		}
		sortEnemyList();
		
		for (int j = 0; j < Main.level.sprites.size(); j++)
		{

			Sprite e = Main.level.sprites.get(j);
			
			if (e.identifier != 'e' && e.identifier != 'f') {
				if (animation2 % 60 < 30)
				{
					if (animation2 % 5 == 0)
						e.yOffset++;
				}
				else {
					if (animation2 % 5 == 0)
						e.yOffset--;
				}
			}
			
			if (e.identifier == 'e' && e.alive) {
				e.attacking = true;
				e.moving = false;
				//e.alerted = true;
				
				double sx = e.x - Main.level.x;
				double sy = Main.level.y - e.y;
				
				double hypot = Math.hypot(sx, sy);
				
				double spx = sx/hypot;
				double spy = sy/hypot;
				
				if (!e.alerted) {
				if (Math.abs(sx) < 256 && Math.abs(sy) < 256) {
					e.alerted = true;
				}
				
				}
				
				if (Math.abs(sx) > 64 && e.alerted)
				{
					e.x -= 2*spx;
					e.attacking = false;
					e.moving = true;
				}
				if (Math.abs(sy) > 64 && e.alerted)
				{
					e.y += 2*spy;
					e.attacking = false;
					e.moving = true;
				}
				if (e.alerted) {
			    if (animation % 30 < 15 && e.moving) {
					e.xOffset = 0;
					e.yOffset = 256;
					e.texSize = 64;
				} else if (animation % 30 < 30 && e.moving) {
					e.xOffset = 64;
					e.yOffset = 256;
					e.texSize = 64;
				} 
				if (animation % 30 < 15 && e.attacking) {
					e.xOffset = 128;
					e.yOffset = 256;
					e.texSize = 128;
				} else if (animation % 30 < 30 && e.attacking) {
					e.xOffset = 0;
					e.yOffset = 320;
					e.texSize = 128;
				} 
				}
				if (Math.abs(sx) <= 64 && Math.abs(sy) <= 64 && animation % 60 == 0){
					
					//System.out.println("WORKINGGGGGG");
					Player.health -= 10;
				}
			}
			
			if (e.identifier == 'e' && !e.alive && !e.endAnimation) {
				//System.out.println("WORKING");
				e.animation++;
				if (e.animation % 60 < 15) {
					e.xOffset = 249;
					e.yOffset = 253;
					e.texSize = 128;
				} else if (e.animation % 60 < 30) {
					e.xOffset = 366;
					e.yOffset = 257;
					e.texSize = 128;
				} else if (e.animation % 60 < 45) {
					e.xOffset = 291;
					e.yOffset = 377;
					e.texSize = 128;
				} else if (e.animation % 60 < 60) {
					e.xOffset = 359;
					e.yOffset = 40;
					e.texSize = 128;
					e.endAnimation = true;
				}
			}
			
			if (e.identifier == 'f') {
				double a = Math.toRadians(e.fireballAngle);
				e.x += 12*Math.cos(a);
				e.y -= 12*Math.sin(a);
				/*
				if (Main.level.map[(int)((e.y+10)/64)][(int)((e.x-10)/64)] != Wall.EMPTY_BLOCK 
						&& Main.level.map[(int)((e.y+10)/64)][(int)((e.x-10)/64)] != Wall.TRIGGER
						&& Main.level.map[(int)((e.y+10)/64)][(int)((e.x-10)/64)] != Wall.DOOR)
				{
					int index = Main.level.sprites.indexOf(e);
					Main.level.sprites.remove(index);
				}*/
				
				for (int i = 0; i < Main.level.sprites.size(); i++) {
					Sprite s = Main.level.sprites.get(i);
					if (s.identifier == 'e') {
						if ((int)(e.y)/64 == (int)(s.y/64) && (int)(e.x/64) == (int)(s.x/64)) {
							s.health -= Player.attack;
							if (s.alive)
								Effects.e.hit.playAsSoundEffect(1.0f, 1.0f, false);
							if (s.alive) {
							int index = Main.level.sprites.indexOf(e);
							if (index != -1)
								Main.level.sprites.remove(index);
							}
							if (s.health <= 0 && s.alive) {
								s.alive = false;
								Player.xp+=10;
							//	Effects.e.death.playAsSoundEffect(1.0f, 1.0f, false);
								try { 
									PCMFilePlayer p = new PCMFilePlayer(new File("sounds/kill.wav"));
									p.start();
								} catch (Exception m) {
									
								}
							}
						}
					}
				}
			}
			
			if ((int)(Main.level.x)/64 == (int)(e.x)/64 && (int)(Main.level.y)/64 == (int)(e.y)/64) {
				if (e.identifier == 'p') {
					Player.potion++;
					//Effects.e.pickup.playAsSoundEffect(1.0f, 1.0f, false);
					try { 
						PCMFilePlayer p = new PCMFilePlayer(new File("sounds/pickup.wav"));
						p.start();
					} catch (Exception m) {
						
					}
					int index = Main.level.sprites.indexOf(e);
					Main.level.sprites.remove(index);
				}
				else if (e.identifier == 'k') {
					Player.key = true;
					//Effects.e.pickup.playAsSoundEffect(1.0f, 1.0f, false);
					try { 
						PCMFilePlayer p = new PCMFilePlayer(new File("sounds/pickup.wav"));
						p.start();
					} catch (Exception m) {
						
					}
					int index = Main.level.sprites.indexOf(e);
					Main.level.sprites.remove(index);
				} else if (e.identifier == 'l') {
					if (Main.level == Level.level1) RenderEnd.end = true;
					animation = 0;
					animation2 = 0;
					Player.key = false;
					RenderMap.open = false;
					RenderMap.timer2 = 0;
					RenderMap.tick = false;
					RenderMap.c = 0;
					RenderMap.timer = 0;
				}
			}
			
			double heightAndWidth = ((Main.DISTANCE_TO_PROJECTION*e.texSize) / e.distance);
	
			double sliceProjectedSliceHeight = heightAndWidth;
			double sliceProjectedSliceWidth = heightAndWidth;

			int startTexture = (int) ((Main.WIDTH>>1) - (sliceProjectedSliceWidth / 2) + (e.angle * Main.WIDTH/Player.FOV));
			int endTexture = startTexture + (int) (sliceProjectedSliceWidth);
			double divisor = (double) (endTexture - startTexture) / e.texSize;

			double counter = 0;
			if (startTexture < 0)
				counter = Math.abs(startTexture);

			for (int i = 0; i < Main.WIDTH; i++)
			{

				if (i >= startTexture && i <= endTexture)
				{
					if (e.distance < RenderMap.zBuffer[i])
					{
						int sub = (int) (counter / (divisor + 0.01));
						
						float texX = (sub+e.xOffset)/512.0f;
						float texX_1 = (sub+1+e.xOffset)/512.0f;
						float texY = (e.yOffset)/512.0f;
						float texY_64 = (e.yOffset+e.texSize)/512.0f;
						float minus = (float) ((Main.HEIGHT>>1)-sliceProjectedSliceHeight/2);
						float plus = (float) ((Main.HEIGHT>>1)+sliceProjectedSliceHeight/2);
						int fog_amount = 1000;
						glColor3f((float)(e.r/e.distance*fog_amount), (float)(e.g/e.distance*fog_amount), (float)(e.b/e.distance*fog_amount));
						
						glBegin(GL_QUADS);
							glTexCoord2f(texX, texY_64);
							glVertex2f(i, plus);
							
							glTexCoord2f(texX, texY);
							glVertex2f(i, minus);
							
							glTexCoord2f(texX_1, texY);
							glVertex2f(i+1, minus);
							
						    glTexCoord2f(texX_1, texY_64);
							glVertex2f(i+1, plus);
						glEnd();
					}
					counter++;
				}
			}
		}
	}
	
	/**
	 * Uses selection sort to sort the list of enemies from furthest to closest.
	 */

	private static void sortEnemyList()
	{
		int size = Main.level.sprites.size();
		
		for (int i = 0; i < size - 1; i++)
		{
			for (int j = i + 1; j < size; j++)
			{

				if (Main.level.sprites.get(i).distance < Main.level.sprites.get(j).distance)
				{
					Sprite e = Main.level.sprites.get(i);
					Main.level.sprites.set(i, Main.level.sprites.get(j));
					Main.level.sprites.set(j, e);
				}
			}
		}
	}

	/**
	 * This method checks to see if the enemy is in the field of view of the
	 * player and returns an appropriate boolean response.
	 * 
	 * @return true if the enemy is in the player's field of view.
	 */

	private static void checkEnemy(Sprite e)
	{
		double angle_in_radians = Math.toRadians(Main.level.angle);
		
		double dX = Math.cos(angle_in_radians);
		double dY = Math.sin(angle_in_radians);

		double vX = e.x - Main.level.x;
		double vY = Main.level.y - e.y;
		
		double hypot2 = Math.hypot(vX, vY);

		e.distance = hypot2;

		double vPrimeX = vX / hypot2;

		e.angleWithOrigin = Math.toDegrees(Math.acos(vPrimeX));
		
		if (vY < 0)
		{
			e.angleWithOrigin = 360 - e.angleWithOrigin;
		}
		
		e.playerViewingAngle = Main.level.angle;
		
		if ((vY < 0 && vX > 0) && (dX > 0 && dY >= 0))
		{
			e.playerViewingAngle = Main.level.angle + 360;
		} 
		else if ((vY > 0 && vX > 0) && (dX > 0 && dY < 0))
		{
			e.angleWithOrigin += 360;
		}
		
		e.angle = e.playerViewingAngle - e.angleWithOrigin;
	}
}
