package texture;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

/**
 * Represents container to store Texture objects. More specifically, represents
 * all of the Textures that will be used inside of the game.
 * 
 * @author btwasilow
 * 
 */

public class Textures
{
	public Texture textures;
	public Texture enemy;
	public Texture handTexture;
	public Texture fireTexture;
	public Texture sprites;

	public static Textures t = new Textures();

	/**
	 * Constructor that loads the textures one by one.
	 */

	public Textures()
	{
		try
		{
			BufferedImage bufferedImage = ImageIO.read(new File("res/sprites/textures.png"));
			textures = BufferedImageUtil.getTexture("", bufferedImage);

			BufferedImage bufferedImage2 = ImageIO.read(new File("res/sprites/wizardEnemy.png"));
			enemy = BufferedImageUtil.getTexture("", bufferedImage2);

			BufferedImage bufferedImage3 = ImageIO.read(new File("res/sprites/handAnimation.png"));
			handTexture = BufferedImageUtil.getTexture("", bufferedImage3);

			BufferedImage bufferedImage4 = ImageIO.read(new File("res/sprites/fireball.png"));
			fireTexture = BufferedImageUtil.getTexture("", bufferedImage4);
			
			BufferedImage bufferedImage5 = ImageIO.read(new File("res/sprites/sprites.png"));
			sprites = BufferedImageUtil.getTexture("", bufferedImage5);
		} catch (Exception e)
		{
			System.out.println("COULD NOT LOAD SPRITESHEETS");
		}
	}
}
