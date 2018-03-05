package logic;

import util.Vector2d;
/**
 * Internal ball representation.
 */
public class Ball {
	
	/* Constants */
	
	private final double size = 20;
	
	/* Private instance variables */
	private Vector2d pos;
	private Vector2d speed;
	
	/**
	 * Default constructor.
	 */
	public Ball() {
		pos = new Vector2d();
		speed = new Vector2d();
	}
	
	/**
	 * Specialised constructor.
	 * @param p initial position
	 * @param s initial velocity
	 */
	public Ball(Vector2d p, Vector2d s) {
		pos = p;
		speed = s;
	}
	
	/**
	 * Getter.
	 * @return the ball's position
	 */
	public Vector2d getPos() {
		return pos;
	}
	
	/**
	 * Setter.
	 * @param p the ball's new position
	 */
	public void setPos(Vector2d p) {
		pos = p;
	}
	
	/**
	 * Getter.
	 * @return the ball's size
	 */
	public double getSize() {
		return size;
	}
	
	/**
	 * Getter.
	 * @return the ball's speed vector
	 */
	public Vector2d getSpeed() {
		return speed;
	}
	
	/**
	 * Setter.
	 * @param s the ball's new speed vector
	 */
	public void setSpeed(Vector2d s) {
		speed = s;
	}
	
	/**
	 * Calculates the ball's position after the current logic cycle.
	 * @param deltaT time since the last cycle
	 * @return the next position
	 */
	public Vector2d getNextPos(double deltaT) {
		return pos.add(speed.scale(deltaT));
	}
	
	/**
	 * Moves the ball one step.
	 * @param deltaT time since the last cycle
	 */
	public void move(double deltaT) {
		pos = getNextPos(deltaT);
	}
	
	/**
	 * Inverts the ball's speed along the x-axis.
	 */
	public void reflectX() {
		speed = speed.reflectX();
	}
	
	/**
	 * Inverts the ball's speed along the y-axis.
	 */
	public void reflectY() {
		speed = speed.reflectY();
	}
	
	/**
	 * Auxiliary getter.
	 * @return The x-coordinate of the ball's left side.
	 */
	public double getLeftX() {
		return pos.getX();
	}
	
	/**
	 * Auxiliary getter.
	 * @return The x-coordinate of the ball's right side.
	 */
	public double getRightX() {
		return pos.getX() + size;
	}
	
	/**
	 * Auxiliary getter.
	 * @return The y-coordinate of the ball's top side.
	 */
	public double getUpperY() {
		return pos.getY();
	}
	
	/**
	 * Auxiliary getter.
	 * @return The y-coordinate of the ball's bottom side.
	 */
	public double getLowerY() {
		return pos.getY() + size;
	}
	
	/**
	 * Speeds up the ball by a factor.
	 * @param factor speedup factor
	 */
	public void speedUp(double factor) {
		speed = speed.scale(factor);
	}
	
}
