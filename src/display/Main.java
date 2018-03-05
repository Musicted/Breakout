package display;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import logic.Block;
import logic.GameLogic;
import logic.Levels;
import util.Vector2d;

/*
 * Unser Programm implementiert eine Version des Breakout-Spiels, mithilfe des
 * "model, view, controller" Prinzips. Durch Nutzen des MVCs ist es möglich das
 * Spiel über verschiedene Views zu visualisieren, in diesem Projekt auf dem 
 * CAU-Hochhaus. Zur Darstellung auf dem Hochhaus haben wir die View auf die Größe des
 * Hochhauses(28 x 14) skaliert. 
 * Als Model haben wir verschiedene Klassen geschrieben, 
 * dazu zählt das Paket logic und das Paket util. 
 * Die View und der Controller werden in dem Paket display realisiert. 
 * Unser Standartcontroller benutzt die Maus, um das Paddle zu steuern.
 * Um zu erreichen, dass das Spiel auf jeder Maschine gleich schnell läuft, nutzen wir
 * in der Main-Klasse die Mainloop, die die Ausführungszeit für ein frame misst, dabei
 * dient die GameLogic unter anderem der konstanten Bewegung des Balls.
 * Als Basis der Grafik wird die ACM Bibliothek genutzt, die bereits in der
 * Vorlesung genutzt wurde. 
 * Ebenfalls wurde in diesem Projekt viel mit objekt-orientierter Programmierung gearbeitet, 
 * es wurden eigene Objekte für den Ball, das Spielbrett, die Blöcke,
 * das Paddle, usw. erstellt und genutzt. 
 * Auch die, für die objekt-orientierte Programmierung, typische Vererbung spielt eine wichtige
 * Rolle in unserem Programm. So ist zum Beipiel die NoBlock-Klasse eine Unterklasse der 
 * Block-Klasse, sowie auch LighthouseScreenThread von LighthouseDisplayThread erbt. 
 * Auch die Gleichzeitigkeit spielt ein Rolle bei der Darstellung verschiedener Views, in unserem
 * Beispiel in der Klasse LightHouseDisplayThread.
 * Zu Beginn des Spiels wird in der Main-Klasse die init-Methode ausgeführt, die die Verbindung
 * zum Lighthouse herstellt, sowie die Möglichkeit Mouseevents zu nutzen und das Spielbrett, bestehend aus
 * Ball, Paddle und Platzhaltern für Blöcke, zu erstellen.
 * Danach wird die run-Methode ausgeführt, die das Intro startet und ein neues Objekt der GameLogic
 * erstellt. Dieses Objekt beinhaltet zunächst das 1. Level. Innerhalb der run-Methode wird die Mainloop
 * gestartet und in Dauerschleife ausgeführt.
 * Die Mainloop ist , wie bereits beschrieben für die zeitliche Abhängigkeit zuständig, darüber hinaus prüft
 * sie auch, ob der Spieler ein Level abgeschlossen oder bereits verloren hat.
 * Die GameLogic beinhaltet drei Konstruktoren, die dazu dienen zwischen Leveln und Mitteilung an den 
 * Nutzer auszuwählen. 
 * Sie beinhaltet verschiedene getter-Methoden, registriert die Kollision des Balls mit
 * den Blöcken, dem Paddle und den Rändern, außerdem ist sie zuständig für die Bewegung des Paddles 
 * und des Balls.
 * In der Board-Klasse haben wir ebenfalls mehrere Konstruktoren, die dazu dienen ein Spielfeld
 * mit unterschiedlichen Farben zu erstellen. Sie nutzt dafür die Block-Klasse und NoBlock-Klasse.
 * Die NoBlock-Klasse, ist wie bereits beschrieben, eine Unterklasse der Block-Klasse und dient vor allem
 * der einfacheren Registierung einer Kollision mit Blöcken.
 * Die Block-Klasse dient der Farbgebung verschiedener Blöcke, diese werden zum Teil zufällig
 * generiert.
 * 
 * 
 * 
 */

public class Main extends GraphicsProgram {
	
	//constants
	private static final int WIDTH = 560;
	private static final int HEIGHT = 280;
	private static final int BLOCK_HEIGHT = 20;
	private static final int BLOCK_WIDTH = 40;
	private static final int FPS = 60;
	private static final long FRAMETIME = 1_000_000_000 / FPS; // nanoseconds
	private static final int FPS_LH = 30;
	private static final long FRAMETIME_LH = 1_000_000_000 / FPS_LH;

	//instance variables
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

//		showText(Levels.INTRO);
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
		paddle.setLocation(paddlePos, HEIGHT - gameLogic.getPaddleData()[2]);

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
				blockReps[col][row] = new GRect(col * BLOCK_WIDTH, row * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
				blockReps[col][row].setFillColor(Color.BLACK);
				blockReps[col][row].setFilled(true);
				add(blockReps[col][row]);
			}
		}

		ball = new GRect(-500, -500, gameLogic.getBallSize(), gameLogic.getBallSize());
		ball.setColor(Color.WHITE);
		ball.setFillColor(Color.WHITE);
		ball.setFilled(true);
		add(ball);

		paddle = new GRect(-500, HEIGHT - gameLogic.getPaddleData()[2], gameLogic.getPaddleData()[1], gameLogic.getPaddleData()[2]);
		paddle.setFillColor(Color.WHITE);
		paddle.setFilled(true);
		add(paddle);

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