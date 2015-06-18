package edu.udel.jogordon.bombsgoboom;

import edu.udel.jogordon.gameframework.Position;

public class Wall extends GameSprite<Wall> {
	private boolean breakable;
	public Wall(Position position, char orientation, boolean breakable) {
        super(position, orientation);
        this.breakable = breakable;
    }

	public boolean getBreakable() {
		return breakable;
	}
	
	public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}

    public Wall copy() {
        return new Wall(getPosition(), getOrientation(), breakable);
    }
    
    public String toString() {
    	return "Position: "+ getPosition() + ", Orientation: "+ getOrientation() + 
    		   ", Breakable: " + breakable + "\n";
    }

}
