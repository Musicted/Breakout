package display;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import logic.Block;
import logic.GameLogic;
import util.Vector2d;

public class Main extends GraphicsProgram {
	private static final int WIDTH = 560;
	private static final int HEIGHT = 280;
	private static final int FPS = 60;
	private static final long FRAMETIME = 1_000_000_000 / FPS; // nanoseconds
	private static final int FPS_LH = 1;
	private static final long FRAMETIME_LH = 1_000_000_000 / FPS_LH;

	private GameLogic gameLogic = new GameLogic();
	private GRect ball;
	private GRect paddle;
	private GRect[][] blockReps;

	private boolean useLighthouse = false;
	private String uname;
	private String token;
	private LighthouseDisplay disp;

	public void init() {
		if (readBoolean("Use Lighthouse API? [true/false] ")) {
			useLighthouse = true;
			 uname = readLine("Username: ");
			 token = readLine("Token: ");
			disp = new LighthouseDisplay(uname, token, 1);

			try {
				disp.connect();
			} catch (Exception e) {
				println("Couldn't connect.");
				e.printStackTrace();
			}
		}

		setSize(WIDTH, HEIGHT);
		addMouseListeners();
		initGFX();
	}

	public void run() {
		mainLoop();
	}

	private void mainLoop() {
		long timer = System.nanoTime();
		long lastFrame = System.nanoTime();
		long lastLHFrame = System.nanoTime();

		while (true) {
			gameLogic.cycle(System.nanoTime() - timer);
			timer = System.nanoTime();

			if (System.nanoTime() - lastFrame > FRAMETIME) {
				display();
				lastFrame = System.nanoTime();
			}

			if (useLighthouse && System.nanoTime() - lastLHFrame > FRAMETIME_LH) {
				displayLighthouse();
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
		paddle.setFillColor(Color.RED);
		paddle.setFilled(true);
		add(paddle);

	}

	private void displayLighthouse() {
		byte[] px = new byte[3 * 28 * 14];
		for (int i = 0; i < px.length; i++) {
			px[i] = 127;
		}

		try {
			disp.send(px);
		} catch (Exception e) {
			println("Couldn't send data.");
			e.printStackTrace();
		}
	}

}