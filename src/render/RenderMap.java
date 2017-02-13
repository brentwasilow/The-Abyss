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

import level.Level;
import main.Main;

import org.lwjgl.input.Keyboard;

import player.Player;
import texture.Textures;
import wall.Wall;
import audio.Effects;
import audio.PCMFilePlayer;

public final class RenderMap
{
//	public static double distance;
	public static double[] zBuffer = new double[Main.WIDTH];
	
	public static int timer = 0;
	public static int timer2 = 0;
	public static int timer3 = 0;
	public static final int TILE_SIZE = 64;
	public static final int TILE_SIZE_SHIFT = 6;
	public static int subimageOffsetVertical;
	public static int subimageOffsetHorizontal;
	public static int textureOffsetHorizontal;
	public static int textureOffsetVertical;
	static int c = 0;
	public static int co;
	public static int ro;
	
	public static boolean wait = false;
	public static boolean tick = false;
	public static boolean open = false;

	/**
	 * Will render the map in a pseudo 3D space using a 2D grid.
	 * 
	 * @param g
	 *            graphics plugin for drawing to the screen.
	 */
	
	public static void render()
	{
		Textures.t.textures.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		double viewingAngle = Main.level.angle + (Player.FOV >> 1);
		
		doorLogic();
		
		if (wait) {
			timer3++;
		}
		
		if (timer3 == 10) {
			wait = false;
			timer3 = 0;
		}
		
		for (int i = 0; i < Main.WIDTH; i++)
		{
			if (viewingAngle >= 360.0)
				viewingAngle -= 360.0;
			if (viewingAngle < 0.0)
				viewingAngle += 360.0;

			double verticalDistance = verticalIntersection(viewingAngle);
			double horizontalDistance = horizontalIntersection(viewingAngle);

			double distance;
			int textureOffset;
			int subimageOffset;

			if (horizontalDistance < verticalDistance)
			{
				distance = horizontalDistance;
				textureOffset = textureOffsetHorizontal;
				subimageOffset = subimageOffsetHorizontal;

			} else
			{
				distance = verticalDistance;
				textureOffset = textureOffsetVertical;
				subimageOffset = subimageOffsetVertical;
			}
			double correctDistance = distance * Math.cos((viewingAngle - Main.level.angle) * Math.PI / 180.0);
			int projectedSliceHeight = (int) ((Main.DISTANCE_TO_PROJECTION << TILE_SIZE_SHIFT) / correctDistance);

			zBuffer[i] = distance;
			
			float color = (float) (1.0/distance*1000);
			
			if (Main.level == Level.level1)
				glColor3f(color, color, color);
			else 
				glColor3f(color, color, color);
			
			float texX = (subimageOffset+textureOffset)/512.0f;
			float texX_1 = (subimageOffset+textureOffset+1)/512.0f;
			float texY_64 = 64/512.0f;
			float minus = (Main.HEIGHT>>1)-projectedSliceHeight/2;
			float plus = (Main.HEIGHT>>1)+projectedSliceHeight/2;
			
			glBegin(GL_QUADS);
				glTexCoord2f(texX, texY_64);
				glVertex2f(i, plus);
				
				glTexCoord2f(texX, 0);
				glVertex2f(i, minus);
				
				glTexCoord2f(texX_1, 0);
				glVertex2f(i+1, minus);
				
			    glTexCoord2f(texX_1, texY_64);
				glVertex2f(i+1, plus);
			glEnd();

			viewingAngle -= Main.ANGLE_BETWEEN_RAYS;
		}
	}

	private static void doorLogic()
	{
		if (timer < 64.0 && tick && !open)
		{
			timer++;
		}
		if (timer == 64.0 && timer2 < 128) {
			timer2++;
			open = true;
		}
		if ((int)Main.level.x>>TILE_SIZE_SHIFT == co && (int)Main.level.y>>TILE_SIZE_SHIFT == ro) {
		} else {
			if (timer2 == 128 && open) {
				timer--;
				if (c == 0) {
					Effects.e.door.playAsSoundEffect(1.0f, .8f, false);
				}
				c++;
			}
		}
		if (timer == 0 && open) {
			open = false;
			timer2 = 0;
			tick = false;
			c = 0;
		}
		
	}

	/**
	 * This method calculates the closest wall utilizing the vertical portion of
	 * the grid.
	 * 
	 * @param viewingAngle
	 *            the angle at which the ray is being cast.
	 * 
	 * @return the distance to the closest wall utilizing the vertical
	 *         intersection of the grid.
	 */

	private static double verticalIntersection(double viewingAngle)
	{
		int verticalX;
		double verticalY;
		int dx;
		double dy;
		double t = Math.tan(viewingAngle * Math.PI / 180.0);

		if (viewingAngle == 90.0 || viewingAngle == 270.0)
		{
			return 100000000.0;
		}

		if (viewingAngle > 90.0 && viewingAngle < 270.0)
		{
			verticalX = (((int) Main.level.x) >> TILE_SIZE_SHIFT) << TILE_SIZE_SHIFT;
			dx = -TILE_SIZE;
			verticalY = Main.level.y + (Main.level.x - verticalX) * t;
			dy = t * TILE_SIZE;
			verticalX--;
		} else
		{
			verticalX = ((((int) Main.level.x) >> TILE_SIZE_SHIFT) << TILE_SIZE_SHIFT) + TILE_SIZE;
			dx = TILE_SIZE;
			verticalY = Main.level.y + (Main.level.x - verticalX) * t;
			dy = -t * TILE_SIZE;
		}
		int column = verticalX >> TILE_SIZE_SHIFT;
		int row = ((int) verticalY) >> TILE_SIZE_SHIFT;

		if (row < 0 || row >= Main.level.map.length || column < 0 || column >= Main.level.map.length)
		{
			return 10000000.0;
		}
		int offset = 0;
		
		while (Main.level.map[row][column] == Wall.EMPTY_BLOCK || Main.level.map[row][column] == Wall.DOOR || Main.level.map[row][column] == Wall.TRIGGER) 
		{	
			if (Main.level.map[row][column] == Wall.DOOR) {
				offset = (int) ((verticalY+(dy/2.0)+.5)%64.0);
				if (ro == row && co == column) {
					if (offset >= timer) break;
				} else {
					break;
				}
			}
			verticalX += dx;
			verticalY += dy;

			column = verticalX >> TILE_SIZE_SHIFT;
			row = ((int) verticalY) >> TILE_SIZE_SHIFT;

			if (row < 0 || row >= Main.level.map.length || column < 0 || column >= Main.level.map.length)
			{
				return 10000000.0;
			}

		}
		textureOffsetVertical = (int) ((verticalY)) - (row << TILE_SIZE_SHIFT);

		if (Main.level.map[row][column] == Wall.COLORED_BRICK)
		{
				subimageOffsetVertical = 128;
		} else if (Main.level.map[row][column] == Wall.PORTAL) {
			subimageOffsetVertical = 256;
		}
		else if (Main.level.map[row][column] == Wall.PHOENIX)
		{
			subimageOffsetVertical = 0;
			int c = ((int) Main.level.x)>> TILE_SIZE_SHIFT;
			int r = ((int) Main.level.y) >> TILE_SIZE_SHIFT;
			
			if (c+1 == column || c-1 == column) {
			
			if (Main.level.map[r][c] == Wall.TRIGGER && Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			{
				if (Main.level.map[r][c+1] == Wall.PHOENIX)	
				{
					Main.level.map[r][c+1] = Wall.EMPTY_BLOCK;
					if (!Effects.e.breakSound.isPlaying())
					{
						if (Effects.e.fire.isPlaying()) Effects.e.fire.stop();
						Effects.e.breakSound.playAsSoundEffect(1.0f, 1.0f, false);
					}
				}
				
				if (Main.level.map[r][c-1] == Wall.PHOENIX)	
				{
					Main.level.map[r][c-1] = Wall.EMPTY_BLOCK;
					if (!Effects.e.breakSound.isPlaying())
					{
						if (Effects.e.fire.isPlaying()) Effects.e.fire.stop();
						Effects.e.breakSound.playAsSoundEffect(1.0f, 1.0f, false);
					}
				}
			}
			}
		} else if (Main.level.map[row][column] == Wall.DOOR)
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Main.level.map[(int)Main.level.y>>TILE_SIZE_SHIFT][(int)(Main.level.x)>>TILE_SIZE_SHIFT] == Wall.TRIGGER)
			{
				//System.out.println("WWW");
				if (Player.key) {
					System.out.println("W");
					ro = row;
					co = column;
					tick  = true;
					if (!open && tick)
					{
						if (Effects.e.fire.isPlaying()) Effects.e.fire.stop();
						Effects.e.door.playAsSoundEffect(1.0f, 0.02f, false);
					}
					} else {
						System.out.println("WW");
						if (!wait) {
							Effects.e.click.playAsSoundEffect(1.0f, 1.0f, false);
							wait = true;
						}
					}
				} 
			subimageOffsetVertical = 192;
			
			double tempDistance = (verticalX+(dx/2.0)-Main.level.x)*(verticalX+(dx/2.0)-Main.level.x)+(verticalY+(dy/2.0)-Main.level.y)*
					(verticalY+(dy/2.0)-Main.level.y);
			if (ro == row && co == column)
				textureOffsetVertical = (int)((verticalY + dy/2.0)-(row << TILE_SIZE_SHIFT)-timer);
			else 
				textureOffsetVertical = (int)((verticalY + dy/2.0)-(row << TILE_SIZE_SHIFT));
	     		return Math.sqrt(tempDistance);	
		}

		double tempDistance = ((verticalX - Main.level.x) * (verticalX - Main.level.x))
				+ ((verticalY - Main.level.y) * (verticalY - Main.level.y));
		return Math.sqrt(tempDistance);
	}

	/**
	 * This method calculates the closest wall utilizing the horizontal portion
	 * of the grid.
	 * 
	 * @param viewingAngle
	 *            the angle at which the ray is being cast.
	 * 
	 * @return the distance to the closest wall utilizing the horizontal
	 *         intersection of the grid.
	 */

	private static double horizontalIntersection(double viewingAngle)
	{
		double horizontalX;
		int horizontalY;
		double dx;
		int dy;
		double t = Math.tan(viewingAngle * Math.PI / 180.0);
		double it = Math.tan(-viewingAngle * Math.PI / 180.0);

		if (viewingAngle == 0.0 || viewingAngle == 180.0)
		{
			return 100000000.0;
		}

		if (viewingAngle > 0.0 && viewingAngle < 180.0)
		{
			horizontalY = (((int) Main.level.y) >> TILE_SIZE_SHIFT) << TILE_SIZE_SHIFT;
			dy = -TILE_SIZE;

			if (viewingAngle == 90.0)
			{
				horizontalX = Main.level.x;
				dx = 0.0;
			} else
			{
				horizontalX = Main.level.x + (Main.level.y - horizontalY) / t;
				dx = TILE_SIZE / t;
			}
			horizontalY--;
		} else
		{
			horizontalY = ((((int) Main.level.y) >> TILE_SIZE_SHIFT) << TILE_SIZE_SHIFT) + TILE_SIZE;
			dy = TILE_SIZE;

			if (viewingAngle == 270.0)
			{
				horizontalX = Main.level.x;
				dx = 0.0;
			} else
			{
				horizontalX = Main.level.x - (Main.level.y - horizontalY) / it;
				dx = TILE_SIZE / it;
			}
		}
		int column = (int) horizontalX >> TILE_SIZE_SHIFT;
		int row = horizontalY >> TILE_SIZE_SHIFT;

		if (row < 0 || row >= Main.level.map.length || column < 0 || column >= Main.level.map.length)
		{
			return 10000000.0;
		}
		int offset = 0;
		while (Main.level.map[row][column] == Wall.EMPTY_BLOCK || Main.level.map[row][column] == Wall.DOOR || Main.level.map[row][column] == Wall.TRIGGER) 
		{	
			if (Main.level.map[row][column] == Wall.DOOR) {
				offset = (int) ((horizontalX+(dx/2.0)+.5)%64.0);
				if (ro == row && co == column) {
					if (offset >= timer) break;
				} else {
					break;
				}
			}
			horizontalX += dx;
			horizontalY += dy;

			column = (int) horizontalX >> TILE_SIZE_SHIFT;
			row = horizontalY >> TILE_SIZE_SHIFT;

			if (row < 0 || row >= Main.level.map.length || column < 0 || column >= Main.level.map.length)
			{
				return 10000000.0;
			}
		}
		textureOffsetHorizontal = (int) (horizontalX) - (column << TILE_SIZE_SHIFT);

		if (Main.level.map[row][column] == Wall.COLORED_BRICK)
		{
				subimageOffsetHorizontal = 128;
		} else if (Main.level.map[row][column] == Wall.PORTAL) {
			subimageOffsetHorizontal = 256;
		}
		else if (Main.level.map[row][column] == Wall.PHOENIX)
		{
			subimageOffsetHorizontal = 0;
			int c = ((int) Main.level.x)>> TILE_SIZE_SHIFT;
			int r = ((int) Main.level.y) >> TILE_SIZE_SHIFT;
			
			if (r+1 == row || r-1 == row) {
			
				if (Main.level.map[r][c] == Wall.TRIGGER && Keyboard.isKeyDown(Keyboard.KEY_SPACE))
				{
					if (Main.level.map[r+1][column] == Wall.PHOENIX) 
					{
						Main.level.map[r+1][column] = Wall.EMPTY_BLOCK;
						if (!Effects.e.breakSound.isPlaying())
						{
							if (Effects.e.fire.isPlaying()) Effects.e.fire.stop();
							Effects.e.breakSound.playAsSoundEffect(1.0f, 1.0f, false);
						}
					}
					
					if (Main.level.map[r-1][column] == Wall.PHOENIX) 
					{
						Main.level.map[r-1][column] = Wall.EMPTY_BLOCK;
						if (!Effects.e.breakSound.isPlaying())
						{
							if (Effects.e.fire.isPlaying()) Effects.e.fire.stop();
							Effects.e.breakSound.playAsSoundEffect(1.0f, 1.0f, false);
						}
					}
				}
			}
		} else if (Main.level.map[row][column] == Wall.DOOR)
		{		
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Main.level.map[(int)Main.level.y>>TILE_SIZE_SHIFT][(int)(Main.level.x)>>TILE_SIZE_SHIFT] == Wall.TRIGGER)
			{
				if (Player.key) {
				ro = row;
				co = column;
				tick  = true; // start timing
				if (!open && tick) //if not completely open 
				{
					Effects.e.door.playAsSoundEffect(1.0f, 0.02f, false); //play sound effect
				}
				} else {
					if (!Effects.e.click.isPlaying() && !wait) {
						if (Effects.e.fire.isPlaying()) Effects.e.fire.stop();
						Effects.e.click.playAsSoundEffect(1.0f, 1.0f, false);
						wait = true;
					}
				}
			} 
			subimageOffsetHorizontal = 192; //offset texture in sprite sheet
			
			double tempDistance = (horizontalX+(dx/2.0)-Main.level.x)*(horizontalX+(dx/2.0)-Main.level.x)+(horizontalY+(dy>>1)-Main.level.y)*
				(horizontalY+(dy>>1)-Main.level.y);
			if (ro == row && co == column)
				textureOffsetHorizontal = (int)((horizontalX + dx/2.0)-(column << TILE_SIZE_SHIFT)-timer);
			else
				textureOffsetHorizontal = (int)((horizontalX + dx/2.0)-(column << TILE_SIZE_SHIFT));
			return Math.sqrt(tempDistance);	
		}
		double tempDistance = (horizontalX - Main.level.x) * (horizontalX - Main.level.x) + (horizontalY - Main.level.y)
				* (horizontalY - Main.level.y);
		return Math.sqrt(tempDistance);
	}
}
