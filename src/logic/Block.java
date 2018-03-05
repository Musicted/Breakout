package logic;

import java.awt.Color;

import acm.util.RandomGenerator;

/**
 * Internal block representation.
 */
public class Block {
	
	private static final Color[] POSSIBLE_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.YELLOW, Color.MAGENTA};
	
	Color color;
	
	/**
	 * Default constructor.
	 * Constructs a block with a random color.
	 */
	public Block() {
		color = randomColor();
	}
	
	/**
	 * Specialised constructor.
	 * Constructs a block with a non-random color.
	 * @param color
	 */
	public Block(Color color) {
		this.color = color;
	}
	
	/**
	 * Getter.
	 * @return the block's color.
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Utility method: generates a random color.
	 * @return random color
	 */
	public static Color randomColor() {
		RandomGenerator rand = new RandomGenerator();
		int choice = rand.nextInt(POSSIBLE_COLORS.length);
		return POSSIBLE_COLORS[choice];
	}
}
