package level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import main.Main;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import sprite.Sprite;
import wall.Wall;
import color.Color;

public class Level
{
	public List<Sprite> sprites = new ArrayList<Sprite>();
	public Audio song;
	
	public double angle;
	public double x;
	public double y;
	
	public int[][] map;
	
	public static final Level level1 = new Level("res/levels/level1.txt");
	public Level(String levelFile)
	{
		try
		{
			File file = new File(levelFile);
			Scanner inputFile = new Scanner(file);
			angle = Double.parseDouble(inputFile.nextLine());
			loadMap(inputFile.nextLine());
			song = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(inputFile.nextLine()));

			inputFile.close();
		} catch (Exception e) {
			System.out.println("COULD NOT LOAD LEVEL");
		}
	}

	private void loadMap(String mapPath)
	{
		try 
		{	
			BufferedImage image = ImageIO.read(new File(mapPath));
			
			int width = image.getWidth();
			int height = image.getHeight();
			
			map = new int[width][height];

			for (int x = 0; x < width; x++)
			{
				for (int y = 0; y < height; y++)
				{
					int color = image.getRGB(x, y);
					
					if (color == Color.WHITE)
						map[y][x] = Wall.EMPTY_BLOCK;
					else if (color == Color.BLACK)
						map[y][x] = Wall.COLORED_BRICK;
					else if (color == Color.BLUE)
						map[y][x] = Wall.PHOENIX;
					else if (color == Color.CYAN)
						map[y][x] = Wall.DOOR;
					else if (color == Color.YELLOW)
						map[y][x] = Wall.TRIGGER;
					else if (color == Color.GREEN)
						sprites.add(new Sprite(x*64+32, y*64+32, 'k')); //key
					else if (color == Color.MAGENTA)
						sprites.add(new Sprite(x*64+32, y*64+32, 'e')); //enemy
					else if (color == Color.ORANGE)
						sprites.add(new Sprite((x*64+32), (y*64+32), 'p')); //potion
					else if (color == Color.RED)
						sprites.add(new Sprite((x*64+32), (y*64+32), 'l')); //level
					else if (color == Color.GREY)
					{
						this.x = (x*64+32);
						this.y = (y*64+32);
					}
				}
			}
		} 
		catch (IOException e)
		{
			System.out.println("COULD NOT LOAD MAP FILE");
		}
	}
}
