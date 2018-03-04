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
	private static final int WIDTH = 560;
	private static final int HEIGHT = 280;
	private static final int FPS = 60;
	private static final long FRAMETIME = 1_000_000_000 / FPS; // nanoseconds
	private static final int FPS_LH = 30;
	private static final long FRAMETIME_LH = 1_000_000_000 / FPS_LH;

	private GameLogic gameLogic = new GameLogic();
	private GRect ball;
	private GRect paddle;
	private GRect[][] blockReps;

	private boolean useLighthouse = false;
	private LighthouseDisplay disp;

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
	
	public void showText(int[][][] text) {
		
		for (int[][] screen : text) {
			gameLogic = new GameLogic(screen);
			display();
			if (useLighthouse) {
				displayScreenLighthouse();
			}
			pause(1000);
		}
		
	}

	public void run() {

		showText(Levels.INTRO);
		gameLogic = new GameLogic(0);
		mainLoop();
	}

	private void mainLoop() {
		long timer = System.nanoTime();
		long lastFrame = System.nanoTime();
		long lastLHFrame = System.nanoTime();
		long tmpTimer;

		while (true) {
			tmpTimer = System.nanoTime();
			gameLogic.cycle(System.nanoTime() - timer);
			timer = tmpTimer;
			
			// Game over
			if (gameLogic.getDeathState() == true) {
				
				showText(Levels.GAMEOVER);
				gameLogic = new GameLogic(0);
				
			}
			
			// Level completed
			if (gameLogic.getLevelCompleteState() == true) {
				if (gameLogic.getNextLevel() < Levels.LVL.length) {
					gameLogic = new GameLogic(gameLogic.getNextLevel());
				} else {
					showText(Levels.YOU_WIN);
					gameLogic = new GameLogic(0);
				}
			}

			if (System.nanoTime() - lastFrame > FRAMETIME) {
				display();
				lastFrame = System.nanoTime();
			}

			if (useLighthouse && System.nanoTime() - lastLHFrame > FRAMETIME_LH) {
//				displayLighthouse();
				LighthouseDisplayThread t = new LighthouseDisplayThread(gameLogic, disp);
				t.start();
				lastLHFrame = System.nanoTime();
			}
		}

	}

	public void mouseMoved(MouseEvent e) {
		double x = e.getX();
		gameLogic.movePaddle(x - gameLogic.getPaddleData()[1] / 2);
	}

	public void mouseClicked(MouseEvent e) {
		gameLogic.go();
	}

	private void display() {
		Vector2d ballPos = gameLogic.getBallPos();
		ball.setLocation(ballPos.getX(), ballPos.getY());

		double paddlePos = gameLogic.getPaddleData()[0];
		paddle.setLocation(paddlePos, HEIGHT - 20);

		Block[][] blocks = gameLogic.getBlocks();
		for (int col = 0; col < blocks.length; col++) {
			for (int row = 0; row < blocks[col].length; row++) {
				blockReps[col][row].setFillColor(blocks[col][row].getColor());
			}
		}
		repaint();
	}

	private void initGFX() {

		Block[][] blocks = gameLogic.getBlocks();
		blockReps = new GRect[blocks.length][blocks[0].length];

		for (int col = 0; col < blocks.length; col++) {
			for (int row = 0; row < blocks[col].length; row++) {
				blockReps[col][row] = new GRect(col * 40, row * 20, 40, 20);
				blockReps[col][row].setFillColor(Color.BLACK);
				blockReps[col][row].setFilled(true);
				add(blockReps[col][row]);
			}
		}

		ball = new GRect(-500, -500, 20, 20);
		ball.setColor(Color.WHITE);
		ball.setFillColor(Color.WHITE);
		ball.setFilled(true);
		add(ball);

		paddle = new GRect(-500, HEIGHT - 20, gameLogic.getPaddleData()[1], 20);
		paddle.setFillColor(Color.WHITE);
		paddle.setFilled(true);
		add(paddle);

	}

	private void displayLighthouse() {
		/* Display the blocks */
		byte[] px = new byte[3 * 28 * 14];
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 14; j++) {
				Color c = gameLogic.getBlocks()[i][j].getColor();
				byte r = (byte) c.getRed();
				byte g = (byte) c.getGreen();
				byte b = (byte) c.getBlue();
				int first = 6 * i + 3 * 28 * j;
				px[first] = r;
				px[first + 3] = r;
				px[first + 1] = g;
				px[first + 4] = g;
				px[first + 2] = b;
				px[first + 5] = b;
			}
		}
		
		/* Display the ball */
		double ballX = gameLogic.getBallPos().getX();
		double ballY = gameLogic.getBallPos().getY() + 10;
		int qBallX = (int) ballX / 20;
		int qBallY = (int) ballY / 20;
		
		int ballIndex = 3 * qBallX + 3 * 28 * qBallY;
		px[ballIndex] = (byte) 255;
		px[ballIndex + 1] = (byte) 255;
		px[ballIndex + 2] = (byte) 255;
		
		/* Display the paddle */
		double[] paddleData = gameLogic.getPaddleData();
		int paddlePos = (int) paddleData[0] / 20;
		int paddleSize = (int) paddleData[1] / 20;
		int paddleIndex = (13 * 28 + paddlePos) * 3;
		for (int i = 0; i < paddleSize; i++) {
			px[paddleIndex + i * 3] = (byte) 255;
			px[paddleIndex + i * 3 + 1] = (byte) 255;
			px[paddleIndex + i * 3 + 2] = (byte) 255;
		}
		
		/* Send the data */
		try {
			disp.send(px);
		} catch (Exception e) {
			println("Couldn't send data: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void displayScreenLighthouse() {
		byte[] px = new byte[3 * 28 * 14];
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 14; j++) {
				Color c = gameLogic.getBlocks()[i][j].getColor();
				byte r = (byte) c.getRed();
				byte g = (byte) c.getGreen();
				byte b = (byte) c.getBlue();
				int first = 6 * i + 3 * 28 * j;
				px[first] = r;
				px[first + 3] = r;
				px[first + 1] = g;
				px[first + 4] = g;
				px[first + 2] = b;
				px[first + 5] = b;
			}
		}
		
		try {
			disp.send(px);
		} catch (Exception e) {
			println("Couldn't send data: " + e.getMessage());
			e.printStackTrace();
		}
	}

}