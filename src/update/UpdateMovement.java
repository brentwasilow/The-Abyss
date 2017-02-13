package update;

import main.Main;

import org.lwjgl.input.Keyboard;

import render.RenderMap;
import trigonometry.Trigonometry;
import wall.Wall;

/**
 * This class handles updating the players view and movement.
 * More specifically, the player can either move forward or backward as well
 * as change angle of view.
 * 
 * @author Brent Thomas Wasilow
 * 
 */

public class UpdateMovement
{
	/**
	 * Field representing the speed of the movement of the player.
	 */
	
	private static double speed = 5.0;
	
	/**
	 * Handles updating the player's x and y coordinates
	 * as well as angle of view from user input from the keyboard.
	 */
	
	public static void update() {
		
		int row = 0, column = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			Main.level.angle -= 2.0;

			if (Main.level.angle < 0.0)
			{
				Main.level.angle += 360.0;
			}
		} 
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			Main.level.angle += 2.0;

			if (Main.level.angle > 359.0)
			{
				Main.level.angle -= 360.0;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			Main.level.x += speed * (Trigonometry.trig.cosViewingAngle[(int) Main.level.angle]);
			Main.level.y -= speed * (Trigonometry.trig.sinViewingAngle[(int) Main.level.angle]);

			column = (int) (Main.level.x)>>RenderMap.TILE_SIZE_SHIFT;
			row = (int)(Main.level.y)>>RenderMap.TILE_SIZE_SHIFT;

			if (Main.level.map[row][column] != Wall.EMPTY_BLOCK && Main.level.map[row][column] != Wall.TRIGGER)
			{
				if (RenderMap.ro == row && RenderMap.co == column && RenderMap.timer2 <= 128 && RenderMap.timer >= 64) {
					
				} else {
					Main.level.x -= speed * (Trigonometry.trig.cosViewingAngle[(int) Main.level.angle]);
					Main.level.y += speed * (Trigonometry.trig.sinViewingAngle[(int) Main.level.angle]);
				}
			} 
		} if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			Main.level.x -= speed * (Trigonometry.trig.cosViewingAngle[(int)Main.level.angle]);
			Main.level.y -= -speed * (Trigonometry.trig.sinViewingAngle[(int) Main.level.angle]);

			row = (int) Main.level.y >> RenderMap.TILE_SIZE_SHIFT;
			column = (int) Main.level.x >> RenderMap.TILE_SIZE_SHIFT;

			if (Main.level.map[row][column] != Wall.EMPTY_BLOCK && Main.level.map[row][column] != Wall.TRIGGER)
			{
				if (RenderMap.ro == row && RenderMap.co == column && RenderMap.timer2 <= 128 && RenderMap.timer >= 64) {
					
				} else {
				Main.level.x += speed * (Trigonometry.trig.cosViewingAngle[(int) Main.level.angle]);
				Main.level.y += -speed * (Trigonometry.trig.sinViewingAngle[(int) Main.level.angle]);
				}
			}
		}
	}
}
