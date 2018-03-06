package bootstrapper;

import display.Main;

/**
 * Bootstrapper for the Main class.
 * This is only needed for creating a runnable .jar file and has no other uses.
 */
public class Strapper {
	
	public static void main(String[] args) {
		Main app = new Main(20, 100);
		app.start();
	}
	
}
