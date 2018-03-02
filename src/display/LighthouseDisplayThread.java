package display;

import java.awt.Color;

import logic.GameLogic;

public class LighthouseDisplayThread extends Thread implements Runnable {
	
	private GameLogic gameLogic;
	private LighthouseDisplay disp;
	
	public LighthouseDisplayThread(GameLogic g, LighthouseDisplay l) {
		gameLogic = g;
		disp = l;
	}
	
	@Override
	public void run() {
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
			System.out.println("Couldn't send data: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
