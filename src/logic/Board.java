package logic;

import java.awt.Color;

/**
 * Holds the blocks.
 * Handles loading of level arrays and block destruction.
 */
public class Board {
	private final int WIDTH = 14;
	private final int HEIGHT = 14;
	
	private Block[][] blocks;
	
	/**
	 * Empty constructor.
	 * Constructs an empty board.
	 */
	public Board() {
		blocks = new Block[WIDTH][HEIGHT];
		for (int col = 0; col < WIDTH; col++) {
			for (int row = 0; row < HEIGHT; row++) {
				blocks[col][row] = new NoBlock();
			}
		}
		
	}
	
	/**
	 * Array constructor.
	 * Constructs blocks from level array.
	 * @param blockAry an array of (x, y) coordinate pairs
	 */
	public Board(int[][] blockAry) {
		blocks = new Block[WIDTH][HEIGHT];
		for (int col = 0; col < WIDTH; col++) {
			for (int row = 0; row < HEIGHT; row++) {
				blocks[col][row] = new NoBlock();
			}
		}
		
		for (int[] pair : blockAry) {
			if (pair.length != 2) {
				throw new IllegalArgumentException("Entries must be x-y-pairs");
			}
			try {
				blocks[pair[0]][pair[1]] = new Block();
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Index (" + pair[0] + ", " + pair[1] + ") is out of bounds.");
			}
		}
	}
	
	/**
	 * Array and color mode constructor.
	 * Constructs blocks from level array.
	 * @param blockAry an array of (x, y) coordinate pairs
	 * @param colorState color behaviour: {@code 0} to color each block individually, {@code 2} to color each row individually, {@code 1} to color all blocks the same.
	 */
	public Board(int[][] blockAry, int colorState) {
		blocks = new Block[WIDTH][HEIGHT];
		for (int col = 0; col < WIDTH; col++) {
			for (int row = 0; row < HEIGHT; row++) {
				blocks[col][row] = new NoBlock();
			}
		}
		Color setColor = Block.randomColor();
		int[] cache = blockAry[0];
		for (int[] pair : blockAry) {
			if (pair.length != 2) {
				throw new IllegalArgumentException("Entries must be x-y-pairs");
			}
			try {
				switch (colorState) {
					case 0:
						// Each Block gets a different Color
						blocks[pair[0]][pair[1]] = new Block();
						break;
					case 1:
						// All Bricks on Screen gets the same Color
						blocks[pair[0]][pair[1]] = new Block(setColor);
						break;
					case 2:
						// Each row gets a different Color
						if (pair[1] != cache[1]) {
							setColor = Block.randomColor();
						}
						cache = pair;
						blocks[pair[0]][pair[1]] = new Block(setColor);
						break;
					default:
						blocks[pair[0]][pair[1]] = new Block();
						break;
				}
						
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Index (" + pair[0] + ", " + pair[1] + ") is out of bounds.");
			}
		}
	}
	
	/**
	 * Getter.
	 * @return the board's blocks
	 */
	public Block[][] getBlocks() {
		return blocks;
	}
	
	/**
	 * Destroys a block.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void destroyBlock(int x, int y) {
		blocks[x][y] = new NoBlock();
	}
	
	/**
	 * Checks whether the board is, indeed, empty.
	 * @return {@code true} if the board is empty, else {@code false}
	 */
	public boolean isBoardEmpty() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				// A Block that isn't a NoBlock is a Block. A NoNoBlock.
				if (getBlocks()[x][y].getClass() != new NoBlock().getClass()) {
					return false;
				}
			}
		}
		return true;
		
	}
}
