package logic;

import util.Vector2d;

public class Ball {
	private final double size = 20;
	
	private Vector2d pos;
	private Vector2d speed;
	
	public Ball(Vector2d p, Vector2d s) {
		pos = p;
		speed = s;
	}
	
	public Vector2d getPos() {
		return pos;
	}
	
	public void setPos(Vector2d p) {
		pos = p;
	}
	
	public double getSize() {
		return size;
	}
	
	public void setSpeed(Vector2d s) {
		speed = s;
	}
	
	public Vector2d getSpeed() {
		return speed;
	}
		
	public Vector2d getNextPos(double deltaT) {
		return pos.add(speed.scale(deltaT));
	}

	public void move(double deltaT) {
		pos = getNextPos(deltaT);
	}
	
	public void reflectX() {
		speed = speed.reflectX();
	}
	
	public void reflectY() {
		speed = speed.reflectY();
	}
	
	public double getLeftX() {
		return pos.getX();
	}
	
	public double getRightX() {
		return pos.getX() + size;
	}
	
	public double getUpperY() {
		return pos.getY();
	}
	
	public double getLowerY() {
		return pos.getY() + size;
	}
	
	public void speedUp(double factor) {
		speed = speed.scale(factor);
	}
	
}
