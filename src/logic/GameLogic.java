package logic;

import acm.util.RandomGenerator;
import util.Vector2d;

public class GameLogic {

	private static final int WIDTH = 560; // Block width: 40
	private static final int HEIGHT = 280; // Block height: 20
	private static final double[] XSPEEDS = { -0.5, 0.5, -0.3, 0.3, -0.7, 0.7 };

	private RandomGenerator rand = new RandomGenerator();
	private Ball ball = new Ball(new Vector2d(280, 140), new Vector2d(0.5, 0.3));
	private Paddle paddle = new Paddle();
	private Board board = new Board();
	private double speedFactor = 200_000.0; // used internally for slowing or speeding up the game

	private boolean isRunning;

	public GameLogic() {
		isRunning = false;
	}

	public void go() {
		if (!isRunning) {
			double xSpeed = XSPEEDS[rand.nextInt(XSPEEDS.length)];
			double ySpeed = rand.nextDouble(-0.5, -0.4);
			ball.setSpeed(new Vector2d(xSpeed, ySpeed));
		}
		isRunning = true;
	}

	public void cycle(long deltaT) {
		double internalTime = deltaT / speedFactor;

		if (isRunning) {
			detectCollisions(internalTime);
			ball.move(internalTime);
		} else {
			double ballX = getPaddleData()[0] + getPaddleData()[1] / 2 - ball.getSize() / 2;
			double ballY = HEIGHT - getPaddleData()[2] - ball.getSize();
			ball.setPos(new Vector2d(ballX, ballY));
		}
	}

	public Vector2d getBallPos() {
		return ball.getPos();
	}

	public double[] getPaddleData() {
		double[] data = { paddle.getXPos(), paddle.getWidth(), paddle.getHeight() };
		return data;
	}

	private void detectCollisions(double deltaT) {
		/* Left/right screen borders */
		if (ball.getRightX() > WIDTH && ball.getSpeed().getX() > 0) {
			ball.reflectX();
		} else if (ball.getLeftX() < 0 && ball.getSpeed().getX() < 0) {
			ball.reflectX();
		}
		/* Upper/lower screen borders */
		if (ball.getLowerY() > HEIGHT && ball.getSpeed().getY() > 0) {
			ball.reflectY();
			System.out.println("u ded.");
			isRunning = false;
			// TODO subtract life, destroy ball
		} else if (ball.getUpperY() < 0 && ball.getSpeed().getY() < 0) {
			ball.reflectY();
		}
		/* Paddle Collisions */
		if (ball.getRightX() > paddle.getXPos() && ball.getLowerY() > HEIGHT - paddle.getHeight()) {
			if (ball.getLeftX() < paddle.getXPos() + paddle.getWidth() && ball.getSpeed().getY() > 0) {
				ball.reflectY();
			}
		}

		/* Block Collisions */
		double leftX = ball.getLeftX() / 40;
		double rightX = ball.getRightX() / 40;
		double upperY = ball.getUpperY() / 20;
		double lowerY = ball.getLowerY() / 20;

		boolean hasReflected = false;
		boolean reflected;

		reflected = collideCorner(leftX, upperY, hasReflected);
		if (reflected) {
			hasReflected = true;
		}
		reflected = collideCorner(rightX, upperY, hasReflected);
		if (reflected) {
			hasReflected = true;
		}
		reflected = collideCorner(leftX, lowerY, hasReflected);
		if (reflected) {
			hasReflected = true;
		}
		reflected = collideCorner(rightX, lowerY, hasReflected);

	}

	private boolean collideCorner(double x, double y, boolean hasReflected) {
		int xF = (int) Math.floor(x);
		int yF = (int) Math.floor(y);
		if (xF >= 0 && xF < board.getBlocks().length) {
			if (yF >= 0 && yF < board.getBlocks().length) {
				if (board.getBlocks()[xF][yF].getClass() != new NoBlock().getClass()) {
					if (x - xF < y - yF && !hasReflected) {
						ball.reflectY();
					} else if (!hasReflected) {
						ball.reflectX();
					}
					board.destroyBlock(xF, yF);
					return true;
				}
			}
		}
		return false;
	}

	public void movePaddle(double x) {
		if (x < 0) {
			paddle.setPos(0);
		} else if (x + paddle.getWidth() > WIDTH) {
			paddle.setPos(WIDTH - paddle.getWidth());
		} else {
			paddle.setPos(x);
		}
	}

	public Block[][] getBlocks() {
		return board.getBlocks();
	}
}
