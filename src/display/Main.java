package display;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import logic.Block;
import logic.GameLogic;
import logic.Levels;
import util.Vector2d;

public class Main extends GraphicsProgram {
	
	/* Constants */
	
	private static final int WIDTH = 560;
	private static final int HEIGHT = 280;
	private static final int BLOCK_HEIGHT = 20;
	private static final int BLOCK_WIDTH = 40;
	private static final int FPS = 60;
	private static final long FRAMETIME = 1_000_000_000 / FPS; // nanoseconds
	private static final int FPS_LH = 30;
	private static final long FRAMETIME_LH = 1_000_000_000 / FPS_LH;
	
	/* Private instance variables */
	
	private GameLogic gameLogic = new GameLogic();
	private GRect ball;
	private GRect paddle;
	private GRect[][] blockReps;

	private boolean useLighthouse = false;
	private LighthouseDisplay disp;
	
	/**
	 * Reads settings via user input and initialises ACM and Lighthouse displays and the controller.
	 */
	public void init() {
		if (readBoolean("Use Lighthouse API? [true/false] ")) {
			useLighthouse = true;
			String uname = readLine("Username: ");
			String token = readLine("Token: ");
			disp = new LighthouseDisplay(uname, token, 1);

			try {
				disp.connect();
			} catch (Exception e) {
				println("Couldn't connect: " + e.getMessage());
				e.printStackTrace();
			}
			// make sure a connection has been established
			while(!disp.isConnected()) {
				println("Awaiting Connection");
				pause(500);
			}
			println("Connected!");
			
		}

		setSize(WIDTH, HEIGHT);
		addMouseListeners();
		initGFX();
	}
	
	/**
	 * Displays text, or rather a sequence of letters, as block configurations (levels).
	 * @param text the sequence of blocks to display.
	 */
	private void showText(int[][][] text) {
		
		for (int[][] screen : text) {
			/* Load the current letter */
			gameLogic = new GameLogic(screen);
			/* Display via ACM */
			display();
			if (useLighthouse) {
				Thread t = new LighthouseScreenThread(gameLogic, disp);
				t.start();
			}
			pause(750);
		}
		
	}
	
	/**
	 * Loads the first level and shows the intro screen.
	 * Calls main loop.
	 */
	public void run() {
		/* Show intro screen */
		showText(Levels.INTRO);
		/* Load level 0 */
		gameLogic = new GameLogic(0);
		/* Start the main loop */
		mainLoop();
	}
	
	/**
	 * Controls logic and display timings and sequences levels;
	 */
	private void mainLoop() {
		long timer = System.nanoTime();
		long lastFrame = System.nanoTime();
		long lastLHFrame = System.nanoTime();
		long tmpTimer; // necessary for precise timing

		while (true) {
			
			/* Run a single logic cycle */
			tmpTimer = System.nanoTime();
			gameLogic.cycle(System.nanoTime() - timer);
			timer = tmpTimer;
			
			/* Game over check */
			if (gameLogic.getDeathState() == true) {
				
				showText(Levels.GAMEOVER);
				gameLogic = new GameLogic(0); // start over at level 0
				continue;
				
			}
			
			/* Level completed check */
			if (gameLogic.getLevelCompleteState() == true) {
				if (gameLogic.getNextLevel() < Levels.LVL.length) {
					// There are levels left: start the next one
					gameLogic = new GameLogic(gameLogic.getNextLevel());
				} else {
					showText(Levels.YOU_WIN);
					gameLogic = new GameLogic(0);
				}
				continue;
			}
			
			/* Update ACM display as needed */
			if (System.nanoTime() - lastFrame > FRAMETIME) {
				display();
				lastFrame = System.nanoTime();
			}
			
			/* Update Lighthouse display as needed */
			if (useLighthouse && System.nanoTime() - lastLHFrame > FRAMETIME_LH) {
				LighthouseDisplayThread t = new LighthouseDisplayThread(gameLogic, disp);
				t.start();
				lastLHFrame = System.nanoTime();
			}
		}

	}
	
	/**
	 * Handles mouse movement.
	 */
	public void mouseMoved(MouseEvent e) {
		double x = e.getX();
		gameLogic.movePaddle(x - gameLogic.getPaddleData()[1] / 2);
	}
	
	/**
	 * Handles mouse input.
	 */
	public void mouseClicked(MouseEvent e) {
		gameLogic.go();
	}
	
	/**
	 * Updates the ACM display.
	 */
	private void display() {
		Vector2d ballPos = gameLogic.getBallPos();
		ball.setLocation(ballPos.getX(), ballPos.getY());

		double paddlePos = gameLogic.getPaddleData()[0];
		paddle.setLocation(paddlePos, HEIGHT - gameLogic.getPaddleData()[2]);

		Block[][] blocks = gameLogic.getBlocks();
		for (int col = 0; col < blocks.length; col++) {
			for (int row = 0; row < blocks[col].length; row++) {
				blockReps[col][row].setFillColor(blocks[col][row].getColor());
			}
		}
		repaint();
	}
	/**
	 * Initialises all graphics objects of the ACM display.
	 */
	private void initGFX() {
		
		/* init the block representations */
		Block[][] blocks = gameLogic.getBlocks();
		blockReps = new GRect[blocks.length][blocks[0].length];

		for (int col = 0; col < blocks.length; col++) {
			for (int row = 0; row < blocks[col].length; row++) {
				blockReps[col][row] = new GRect(col * BLOCK_WIDTH, row * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
				blockReps[col][row].setFillColor(Color.BLACK);
				blockReps[col][row].setFilled(true);
				add(blockReps[col][row]);
			}
		}
		
		/* init the ball representation */
		ball = new GRect(-500, -500, gameLogic.getBallSize(), gameLogic.getBallSize());
		ball.setColor(Color.WHITE);
		ball.setFillColor(Color.WHITE);
		ball.setFilled(true);
		add(ball);
		
		/* init the paddle representation */
		paddle = new GRect(-500, HEIGHT - gameLogic.getPaddleData()[2], gameLogic.getPaddleData()[1], gameLogic.getPaddleData()[2]);
		paddle.setFillColor(Color.WHITE);
		paddle.setFilled(true);
		add(paddle);

	}

}