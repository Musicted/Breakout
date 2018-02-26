package logic;

public class Paddle {
	private final double WIDTH = 70;
	private final double HEIGHT = 20;
	private double xPos;
	
	public Paddle() {
		xPos = 0;
	}
	
	public void setPos(double x) {
		xPos = x;
	}
	
	public double getXPos() {
		return xPos;
	}
	
	public double getWidth() {
		return WIDTH;
	}
	
	public double getHeight() {
		return HEIGHT;
	}
}
