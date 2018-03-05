package display;

import logic.GameLogic;

/**
 * Does what its ancestor does, only less.
 * Created to avoid displaying an out-of-place ball or paddle while displaying text.
 */
public class LighthouseScreenThread extends LighthouseDisplayThread {
	
	/**
	 * Constructor.
	 * @param g GameLogic object to fetch game state information from
	 * @param l LighthouseDisplay to send the data to.
	 */
	public LighthouseScreenThread(GameLogic g, LighthouseDisplay l) {
		super(g, l);
	}
	
	/**
	 * Does exactly what the overridden Method does, only less.
	 */
	@Override
	public void run() {
		dispBlocks();
		sendData();
	}
	
}
