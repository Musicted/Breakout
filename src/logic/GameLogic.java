package logic;

// Notice how we didn't even import any display classes. Like a boss.
import acm.util.RandomGenerator;
import util.Vector2d;

/**
 * Main class of the M-bit of MVC.
 * Handles all 'physical' events and is the single point of communication between controller and view and the rest of the model.
 */
public class GameLogic {
	
	/* Constants */
	
	private static final double FORTYFIVE = 0.785; // ~45 degrees in rad
	private static final int WIDTH = 560;
	private static final int HEIGHT = 280;
	private static final int BLOCK_HEIGHT = 20;
	private static final int BLOCK_WIDTH = 40;
	public static final int RANDOMCOLOR = 0;
	public static final int LETTERCOLOR = 1;
	public static final int ROWSCOLOR = 2;
	
	/* Private instance variables */
	
	private RandomGenerator rand = new RandomGenerator();
	private double speedFactor = 1_000_000.0; // used internally for slowing or speeding up the game
	private Ball ball;
	private Paddle paddle;
	private Board board;

	private boolean isRunning;
	
	private int lifeCount = 3;
	private int currentLevel;
	private boolean levelCompleted = false;
	
	/**
	 * Default constructor.
	 * This must be called prior to the initialisation of the ACM display members in order to avoid NullPointerExceptions.
	 * Has no other uses.
	 */
	public GameLogic() {
		isRunning = false;
		ball = new Ball(new Vector2d(280, 140), new Vector2d(0, 0));
		paddle = new Paddle();
		board = new Board();
	}
	
	/**
	 * Level constructor. Loads a specific level.
	 * @param level index of the level to be loaded.
	 */
	public GameLogic(int level) {
		isRunning = false;
		currentLevel = level;
		ball = new Ball(new Vector2d(280, 140), new Vector2d(0, 0));
		paddle = new Paddle();
		board = new Board(Levels.LVL[level], ROWSCOLOR);
	}
	
	/**
	 * Screen constructor. Loads a 2D-Array in order to display it as a screen (text, etc.).
	 * @param screen an array of (x, y) coordinate pairs.
	 */
	public GameLogic(int[][] screen) {
		isRunning = false;
		ball = new Ball(new Vector2d(-500, -500), new Vector2d(0, 0));
		paddle = new Paddle();
		paddle.setPos(-500);
		board = new Board(screen, LETTERCOLOR);
	}
	
	/**
	 * Starts the ball moving at level start or after losing the ball.
	 * Is called by the controller.
	 */
	public void go() {
		if (!isRunning) {
			Vector2d speed = new Vector2d(0, -0.25);
			speed = speed.rotate(rand.nextDouble(-FORTYFIVE, FORTYFIVE));
			ball.setSpeed(speed);
		}
		isRunning = true;
	}
	
	/**
	 * Calculates all movements and interactions in a given time span.
	 * @param deltaT length of the time span; typically time since last call.
	 */
	public void cycle(long deltaT) {
		double internalTime = deltaT / speedFactor;

		if (isRunning) {
			detectCollisions();
			ball.move(internalTime);
		} else {
			// the ball is supposed to still be on the player's paddle
			double ballX = getPaddleData()[0] + getPaddleData()[1] / 2 - ball.getSize() / 2;
			double ballY = HEIGHT - getPaddleData()[2] - ball.getSize();
			ball.setPos(new Vector2d(ballX, ballY));
		}
		
		/* Level completion check */
		if (board.isBoardEmpty()) {
			isRunning = false;
			levelCompleted = true;
			System.out.println("Level complete");
		}
	}
	
	/**
	 * Forwards the ball's position.
	 * @return to sender.
	 */
	public Vector2d getBallPos() {
		return ball.getPos();
	}

	/**
	 * Forwards the paddle's data.
	 * @return [x-position, width, height]
	 */
	public double[] getPaddleData() {
		double[] data = { paddle.getXPos(), paddle.getWidth(), paddle.getHeight() };
		return data;
	}
	
	/**
	 * Determines whether all lives have been used up.
	 * @return {@code true} if all lives have been used up, else {@code false}
	 */
	public boolean getDeathState() {
		return (lifeCount == 0);
	}
	
	/**
	 * Determines whether all blocks have been removed.
	 * @return {@code true} if all blocks have been removed, else {@code false}
	 */
	public boolean getLevelCompleteState() {
		return levelCompleted;
	}
	
	/**
	 * Finds the index of the next level.
	 * @return the index
	 */
	public int getNextLevel() {
		return currentLevel + 1;
	}
	
	/**
	 * Detects and handles collisions between ball and paddle and screen borders.
	 */
	private void detectCollisions() {
		/* Left/right screen borders */
		if (ball.getRightX() > WIDTH && ball.getSpeed().getX() > 0) {
			ball.reflectX();
		} else if (ball.getLeftX() < 0 && ball.getSpeed().getX() < 0) {
			ball.reflectX();
		}
		/* Upper/lower screen borders */
		// lower border
		if (ball.getLowerY() > HEIGHT && ball.getSpeed().getY() > 0) {
			ball.reflectY();
			isRunning = false;
			lifeCount--;
			System.out.println("You have " + lifeCount + " lives left.");
		// upper border
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
		
		// I really wish we'd found a better solution.
		// This was the project's major challenge, really.
		
		double leftX = ball.getLeftX() / BLOCK_WIDTH;
		int leftXF = (int) Math.floor(leftX);
		double rightX = ball.getRightX() / BLOCK_WIDTH;
		int rightXF = (int) Math.floor(rightX);
		double upperY = ball.getUpperY() / BLOCK_HEIGHT;
		int upperYF = (int) Math.floor(upperY);
		double lowerY = ball.getLowerY() / BLOCK_HEIGHT;
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
		
		
		

	}
	
	/**
	 * Checks whether there is a block at (x, y)
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return {@code true} if a collision occurred, else {@false}
	 */
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
	
	/**
	 * Forwards paddle movement to the actual paddle.
	 * @param x new x-coordinate.
	 */
	public void movePaddle(double x) {
		if (x < 0) {
			paddle.setPos(0);
		} else if (x + paddle.getWidth() > WIDTH) {
			paddle.setPos(WIDTH - paddle.getWidth());
		} else {
			paddle.setPos(x);
		}
	}
	
	/**
	 * Forwards the board's blocks.
	 * @return the board's blocks
	 */
	public Block[][] getBlocks() {
		return board.getBlocks();
	}
	
	/** 
	 * Forwards the ball's size
	 * @return the ball's sizes
	 */
	public double getBallSize() {
		return ball.getSize();
	}
}
