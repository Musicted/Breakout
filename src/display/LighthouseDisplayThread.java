package display;

import java.awt.Color;

import logic.GameLogic;

/**
 * Constructs a 14 by 28 px view of the game state and sends it to the LighthouseDisplay.
 */
public class LighthouseDisplayThread extends Thread implements Runnable {
	
	private GameLogic gameLogic;
	private LighthouseDisplay disp;
	private byte[] px;
	
	/**
	 * Constructor.
	 * @param g GameLogic object to fetch game state information from
	 * @param l LighthouseDisplay to send the data to.
	 */
	public LighthouseDisplayThread(GameLogic g, LighthouseDisplay l) {
		gameLogic = g;
		disp = l;
		px = new byte[3 * 14 * 28];
	}
	
	/**
	 * Method called upon thread start.
	 * Calls all other methods.
	 */
	@Override
	public void run() {
		dispBlocks();
		dispBall();
		dispPaddle();
		sendData();
	}
	
	/**
	 * Constructs Block representations.
	 * Writes into {@code px}
	 */
	void dispBlocks() {
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
	}
	
	/**
	 * Constructs ball representation.
	 * Writes into {@code px}
	 */
	void dispBall() {
		double ballX = gameLogic.getBallPos().getX();
		double ballY = gameLogic.getBallPos().getY() + 10;
		int qBallX = (int) ballX / 20;
		int qBallY = (int) ballY / 20;
		
		int ballIndex = 3 * qBallX + 3 * 28 * qBallY;
		px[ballIndex] = (byte) 255;
		px[ballIndex + 1] = (byte) 255;
		px[ballIndex + 2] = (byte) 255;	
	}
	
	/**
	 * Constructs paddle representation.
	 * Writes into {@code px}
	 */
	void dispPaddle() {
		double[] paddleData = gameLogic.getPaddleData();
		int paddlePos = (int) paddleData[0] / 20;
		int paddleSize = (int) paddleData[1] / 20;
		int paddleIndex = (13 * 28 + paddlePos) * 3;
		for (int i = 0; i < paddleSize; i++) {
			px[paddleIndex + i * 3] = (byte) 255;
			px[paddleIndex + i * 3 + 1] = (byte) 255;
			px[paddleIndex + i * 3 + 2] = (byte) 255;
		}
	}
	
	/**
	 * Sends px to its LighthouseDisplay.
	 */
	void sendData() {
		try {
			disp.send(px);
		} catch (Exception e) {
			System.out.println("Couldn't send data: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
