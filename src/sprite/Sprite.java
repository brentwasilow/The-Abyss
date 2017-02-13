package sprite;

import main.Main;

import org.newdawn.slick.opengl.Texture;

/**
 * Represents the encapsulation of a sprite. More specifically, 
 * a sprite needs fields for displaying properly as well as a texture
 * and corresponding offset to determine which subset of the texture
 * depending on animation. The key optimization is ordering fields
 * from biggest to smallest in order to keep padding to a minimum. 
 * 
 * @author Brent Thomas Wasilow
 *
 */

public class Sprite
{
	public Texture textures;
	
	public double angleWithOrigin;
	public double playerViewingAngle;
	public double angle;
	public double x;
	public double y;
	public double distance;
	public double fireballAngle;
	
	public float r;
	public float g;
	public float b;
	public float a;
	
	public int animation;
	public int xOffset;
	public int yOffset;
	public int texSize;
	public int attack;
	public int health;
	
	public boolean alive;
	public boolean alerted;
	public boolean endAnimation;
	public boolean moving;
	public boolean attacking;
	
	public char identifier;
	
	public Sprite(int x, int y, char identifier) 
	{
		this.x = x;
		this.y = y;
		this.identifier = identifier;
		
		animation = 0;
		endAnimation = false;
		
		if (identifier == 'k') {
			xOffset = 0;
			yOffset = 0;
			texSize = 64;
			r = 1.0f;
			g = 1.0f;
			b = 0.0f;
			a = 1.0f;
		} else if (identifier == 'p') {
			xOffset = 128;
			yOffset = 0;
			texSize = 64;
			r = 0.0f;
			g = 1.0f;
			b = 1.0f;
			a = 1.0f;
		} else if (identifier == 'l') {
			xOffset = 192;
			yOffset = 0;
			texSize = 64;
			r = 1.0f;
			g = 1.0f;
			b = 1.0f;
			a = 1.0f;
		} else if (identifier == 'e') {
			alive = true;
			moving = false;
			attacking = false;
			alerted = false;
			attack = 25;
			health = 100;
			xOffset = 0;
			yOffset = 256;
			texSize = 64;
			r = 1.0f;
			g = 1.0f;
			b = 1.0f;
			a = 1.0f;
		} else if (identifier == 'f') {
			fireballAngle = Main.level.angle;
			xOffset = 256;
			yOffset = 0;
			texSize = 64;
			r = 1.0f;
			g = 1.0f;
			b = 1.0f;
			a = 1.0f;
		}
	}
}
