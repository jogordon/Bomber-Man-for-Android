package edu.udel.jogordon.bombsgoboom;

import edu.udel.jogordon.gameframework.Position;

public class Explosion extends GameSprite {
	private int timer;				// Fireballs only around for limited time
	private String upgrade;			// If bombs that created these were upgraded they appear at greater range

	protected Explosion(Position position, char orientation, String upgrade, int timer) {
		super(position, orientation);
		this.upgrade = upgrade;
		this.timer = timer;
	}
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}
	public String getUpgrade() {
		return upgrade;
	}
	public void onTick(GameState state) {

		timer -= 1;
    	
	}
	@Override
	public Explosion copy() {
		return new Explosion(getPosition(), getOrientation(), upgrade, timer);
	}

}
