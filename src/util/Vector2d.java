package util;

public class Vector2d {
	private double x, y;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Vector2d add(Vector2d other) {
		return new Vector2d(x + other.x, y + other.y);
	}
	
	public Vector2d scale(double s) {
		return new Vector2d(s * x, s * y);
	}
	
	public Vector2d reflectX() {
		return new Vector2d(-x, y);
	}
	
	public Vector2d reflectY() {
		return new Vector2d(x, -y);
	}
	
	public Vector2d rotate(double radians) {
		double xNew = x * Math.cos(radians) - y * Math.sin(radians);
		double yNew = y * Math.cos(radians) + x * Math.sin(radians);
		return new Vector2d(xNew, yNew);
	}
	
	public double abs() {
		return Math.sqrt(x * x + y * y);
	}
}
