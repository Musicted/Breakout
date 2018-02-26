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
	
	private GameLogic gameLogic = new GameLogic();
	private GRect ball;
	private GRect paddle;
	private GRect[][] blockReps;
	
	public void init() {
		setSize(WIDTH, HEIGHT);
		addMouseListeners();
		initGFX();
	}
	
	public void run() {
		mainLoop();
	}
	
	private void mainLoop() {
		long timer = System.nanoTime();
		
		
		while (true) {
			gameLogic.cycle(System.nanoTime() - timer);
			timer = System.nanoTime();
			
			display();
			// TODO limit time between display updates
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
//				blockReps[col][row].setFillColor(Color.cyan);
			}
		}
		repaint();
	}
	
	public void initGFX() {
		
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
}