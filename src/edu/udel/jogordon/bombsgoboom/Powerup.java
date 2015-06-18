package edu.udel.jogordon.bombsgoboom;

import edu.udel.jogordon.gameframework.Position;

public class Powerup extends GameSprite<Powerup>{
	
	private String upgrade;
	
	public Powerup(Position position, char orientation, String upgrade) {
        super(position, orientation);
        this.upgrade = upgrade;
    }

	public String getUpgrade() {
		return upgrade;
	}
	public void setUpgrdae(String upgrade) {
		this.upgrade = upgrade;
	}
    public Powerup copy() {
        return new Powerup(getPosition(), getOrientation(), upgrade);
    }
    public String toString() {
    	return "Position: "+ getPosition() + ", Orientation: "+ getOrientation() + "\n";
    }
}
