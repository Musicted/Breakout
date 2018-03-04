package logic;

import java.awt.Color;

public class Board {
	private final int WIDTH = 14;
	private final int HEIGHT = 14;
	
	private Block[][] blocks;
	
	public Board() {
		blocks = new Block[WIDTH][HEIGHT];
		for (int col = 0; col < WIDTH; col++) {
			for (int row = 0; row < HEIGHT; row++) {
				blocks[col][row] = new NoBlock();
			}
		}
		
		for (int col = 0; col < WIDTH; col++) {
			for (int row = 0; row < 3; row++) {
				blocks[col][row] = new Block();
			}
		}
		
	}
	
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
	
	public Block[][] getBlocks() {
		return blocks;
	}
	
	public void destroyBlock(int x, int y) {
		blocks[x][y] = new NoBlock();
	}
	
	public boolean isBoardEmpty() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (getBlocks()[x][y].getClass() != new NoBlock().getClass()) {
					return false;
				}
			}
		}
		return true;
		
	}
}
