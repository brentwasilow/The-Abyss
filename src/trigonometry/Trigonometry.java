package trigonometry;

public class Trigonometry
{
	/**
	 * The pre-computed cosine values for calculating the change
	 * in x and y when determining movement. Increases efficiency since it
	 * only has to be calculated at the start of the program.
	 */
	
	public double[] cosViewingAngle = new double[360];
	
	/**
	 * The pre-computed sine values for calculating the change
	 * int x and y when determining movement. Increases efficiency
	 * since it only has to be calculated at the start of the program. 
	 */
	
	public double[] sinViewingAngle = new double[360];
	
	/**
	 * Static Trigonometry object to be utilized for calculations.
	 */
	
	public static Trigonometry trig = new Trigonometry();
	
	/**
	 * Initializes the viewing angle arrays for values between 0 and 360.
	 */
	
	public Trigonometry() {
		for (int i = 0; i < 360; i += 1) 				
		{
			cosViewingAngle[i] = Math.cos(i*Math.PI/180.0); 
			sinViewingAngle[i] = Math.sin(i*Math.PI/180.0);
		}
	}
}
