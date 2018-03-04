package logic;

import java.awt.Color;

import acm.util.RandomGenerator;

public class Block {
	Color color;
	
	public Block() {
		color = randomColor();
	}
	
	public Block(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public static Color randomColor() {
		RandomGenerator rand = new RandomGenerator();
		int choice = rand.nextInt(3);
		switch (choice) {
		case 0:
			return Color.RED;
		case 1:
			return Color.BLUE;
		case 2:
			return Color.GREEN;
		}
		return Color.PINK;
	}
}
