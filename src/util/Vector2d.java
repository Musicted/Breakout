package util;

/** 
 * Auxiliary class for 2-dimensional double vectors.
 */
public class Vector2d {
	private double x, y;
	
	/**
	 * Default (0-vector) constructor.
	 */
	public Vector2d() {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Constructor.
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter.
	 * @return x-coordinate
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Getter.
	 * @return y-coordinate
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Vector addition.
	 * @param other other vector
	 * @return the two vectors' sum
	 */
	public Vector2d add(Vector2d other) {
		return new Vector2d(x + other.x, y + other.y);
	}
	
	/** 
	 * Scalar multiplication.
	 * @param s scalar
	 * @return the scaled vector
	 */
	public Vector2d scale(double s) {
		return new Vector2d(s * x, s * y);
	}
	
	/**
	 * Inverts the x-component.
	 * @return vector with inverted x-component
	 */
	public Vector2d reflectX() {
		return new Vector2d(-x, y);
	}
	
	/**
	 * Inverts the y-component.
	 * @return vector with inverted y-component
	 */
	public Vector2d reflectY() {
		return new Vector2d(x, -y);
	}
	
	/**
	 * Rotates the vector.
	 * @param radians Angle of rotation (radians; CCW)
	 * @return the rotated vector
	 */
	public Vector2d rotate(double radians) {
		double xNew = x * Math.cos(radians) - y * Math.sin(radians);
		double yNew = y * Math.cos(radians) + x * Math.sin(radians);
		return new Vector2d(xNew, yNew);
	}
	
	/**
	 * Calculates the absolute length.
	 * @return absolute length
	 */
	public double abs() {
		return Math.sqrt(x * x + y * y);
	}
}
