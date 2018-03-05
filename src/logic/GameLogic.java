package logic;

import acm.util.RandomGenerator;
import util.Vector2d;

public class GameLogic {
	
	private static final double FORTYFIVE = 0.785; // ~45 degrees in rad
	private static final int WIDTH = 560; // Block width: 40
	private static final int HEIGHT = 280; // Block height: 20
	
	public static final int RANDOMCOLOR = 0;
	public static final int LETTERCOLOR = 1;
	public static final int ROWSCOLOR = 2;
	
	private RandomGenerator rand = new RandomGenerator();
	private double speedFactor = 1_000_000.0; // used internally for slowing or speeding up the game
	private Ball ball;
	private Paddle paddle;
	private Board board;

	private boolean isRunning;
	
	private int lifeCount = 3;
	private int currentLevel;
	private boolean levelCompleted = false;

	public GameLogic() {
		isRunning = false;
		ball = new Ball(new Vector2d(280, 140), new Vector2d(0, 0));
		paddle = new Paddle();
		board = new Board();
	}
	
	public GameLogic(int level) {
		isRunning = false;
		currentLevel = level;
		ball = new Ball(new Vector2d(280, 140), new Vector2d(0, 0));
		paddle = new Paddle();
		board = new Board(Levels.LVL[level], ROWSCOLOR);
	}
	
	public GameLogic(int[][] screen) {
		isRunning = false;
		ball = new Ball(new Vector2d(-500, -500), new Vector2d(0, 0));
		paddle = new Paddle();
		paddle.setPos(-500);
		board = new Board(screen, LETTERCOLOR);
	}

	public void go() {
		if (!isRunning) {
			Vector2d speed = new Vector2d(0, -0.25);
			speed = speed.rotate(rand.nextDouble(-FORTYFIVE, FORTYFIVE));
			ball.setSpeed(speed);
		}
		isRunning = true;
	}

	public void cycle(long deltaT) {
		double internalTime = deltaT / speedFactor;

		if (isRunning) {
			detectCollisions();
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
	
	public boolean getDeathState() {
		return (lifeCount == 0);
	}
	
	public boolean getLevelCompleteState() {
		return levelCompleted;
	}
	
	public int getNextLevel() {
		return currentLevel + 1;
	}

	private void detectCollisions() {
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
			if (lifeCount > 0) {
				lifeCount--;
			} else {
				// TODO
				// Game over
			}
		} else if (ball.getUpperY() < 0 && ball.getSpeed().getY() < 0) {
			ball.reflectY();
		}
		/* Paddle Collisions */
		if (ball.getRightX() > paddle.getXPos() && ball.getLowerY() > HEIGHT - paddle.getHeight()) {
			if (ball.getLeftX() < paddle.getXPos() + paddle.getWidth() && ball.getSpeed().getY() > 0) {
				Vector2d speed = new Vector2d(0, -ball.getSpeed().abs());
				double ballMiddle = (ball.getRightX() + ball.getLeftX()) / 2;
				double paddleMiddle = paddle.getXPos() + paddle.getWidth() / 2;
				// between -1 and 1, approximately
				double deflectionFactor = 2 * (ballMiddle - paddleMiddle) / paddle.getWidth();
				speed = speed.rotate(deflectionFactor * FORTYFIVE);
				ball.setSpeed(speed);
				// It's like those YouTube videos
				// BREAKOUT BUT EVERYTIME THE BALL HITS THE PADDLE IT SPEEDS UP 1%
				ball.speedUp(1.01);
			}
		}
		/* Block Collisions */
		double leftX = ball.getLeftX() / 40;
		int leftXF = (int) Math.floor(leftX);
		double rightX = ball.getRightX() / 40;
		int rightXF = (int) Math.floor(rightX);
		double upperY = ball.getUpperY() / 20;
		int upperYF = (int) Math.floor(upperY);
		double lowerY = ball.getLowerY() / 20;
		int lowerYF = (int) Math.floor(lowerY);
		
		// top collision
		if (ball.getSpeed().getY() < 0) {
			boolean reflected = false;
			if (collideCorner(leftXF, upperYF)) {
				reflected = true;
				board.destroyBlock(leftXF, upperYF);
			}
			if (collideCorner(rightXF, upperYF)) {
				reflected = true;
				board.destroyBlock(rightXF, upperYF);
			}
			if (reflected) {
				ball.reflectY();
			}
		} else {
			// bottom collision
			boolean reflected = false;
			if (collideCorner(leftXF, lowerYF)) {
				reflected = true;
				board.destroyBlock(leftXF, lowerYF);
			}
			if (collideCorner(rightXF, lowerYF)) {
				reflected = true;
				board.destroyBlock(rightXF, lowerYF);
			}
			if (reflected) {
				ball.reflectY();
			}
		}
		// left collision
		if (ball.getSpeed().getX() < 0) {
			boolean reflected = false;
			if (collideCorner(leftXF, upperYF)) {
				reflected = true;
				board.destroyBlock(leftXF, upperYF);
			}
			if (collideCorner(leftXF, lowerYF)) {
				reflected = true;
				board.destroyBlock(leftXF, lowerYF);
			}
			if (reflected) {
				ball.reflectX();
			}
		} else {
			// right collision
			boolean reflected = false;
			if (collideCorner(rightXF, upperYF)) {
				reflected = true;
				board.destroyBlock(rightXF, upperYF);
			}
			if (collideCorner(rightXF, lowerYF)) {
				reflected = true;
				board.destroyBlock(rightXF, lowerYF);
			}
			if (reflected) {
				ball.reflectX();
			}
		}
		
		// Level complete
		if (board.isBoardEmpty()) {
			isRunning = false;
			levelCompleted = true;
			System.out.println("Level complete");
			// TODO Display next Level
		}
		

	}

	private boolean collideCorner(int x, int y) {
		if (x >= 0 && x < board.getBlocks().length) {
			if (y >= 0 && y < board.getBlocks().length) {
				if (board.getBlocks()[x][y].getClass() != new NoBlock().getClass()) {
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
	
	public double getBallSize() {
		return ball.getSize();
	}
}
