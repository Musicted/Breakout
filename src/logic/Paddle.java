package logic;

/**
 * internal paddle representation.
 */
public class Paddle {
	private final double WIDTH = 100;
	private final double HEIGHT = 20;
	private double xPos;
	
	/**
	 * Constructor.
	 */
	public Paddle() {
		xPos = 0;
	}
	
	/**
	 * Setter.
	 * @param x new x-coordinate.
	 */
	public void setPos(double x) {
		xPos = x;
	}
	
	/**
	 * Getter.
	 * @return x-coordinate
	 */
	public double getXPos() {
		return xPos;
	}
	
	/**
	 * Getter.
	 * @return width
	 */
	public double getWidth() {
		return WIDTH;
	}
	
	/**
	 * Getter.
	 * @return height
	 */
	public double getHeight() {
		return HEIGHT;
	}
}
