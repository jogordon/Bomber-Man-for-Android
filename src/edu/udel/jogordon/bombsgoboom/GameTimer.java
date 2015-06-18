package edu.udel.jogordon.bombsgoboom;

public class GameTimer {
	private long startTime; //This tracks the amount of time the level has been going on
	
	public GameTimer() {
		this.startTime = System.currentTimeMillis();
	}

	public int getTimeElapsed() {
		return (int)((System.currentTimeMillis() - startTime) / 1000);

	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
