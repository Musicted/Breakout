package display;

import logic.GameLogic;

public class LighthouseScreenThread extends LighthouseDisplayThread {
	
	public LighthouseScreenThread(GameLogic g, LighthouseDisplay l) {
		super(g, l);
	}
	
	public void run() {
		dispBlocks();
		sendData();
	}
	
}
