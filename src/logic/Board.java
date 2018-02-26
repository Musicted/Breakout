package logic;

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
	
	public Block[][] getBlocks() {
		return blocks;
	}
	
	public void destroyBlock(int x, int y) {
		blocks[x][y] = new NoBlock();
	}
}
