package edu.udel.jogordon.bombsgoboom;

import edu.udel.jogordon.gameframework.Position;

public class Enemy extends GameSprite<Enemy> {
	public Enemy(Position position, char orientation) {
        super(position, orientation);
    }

    public void onTick(GameState state) {
    	//Enemies change to a random direction every 3 ticks on average
    	int randomInt = 1 + (int)(Math.random() * 3);
    	if(randomInt == 3) {
    		int randomInt2 = 1 + (int)(Math.random()*5);
    		if(randomInt2 == 1) {setOrientation('U');}
    		if(randomInt2 == 2) {setOrientation('D');}
    		if(randomInt2 == 3) {setOrientation('L');}
    		if(randomInt2 == 4) {setOrientation('R');}    		
    	}
        move(getOrientation(), state);
    }

    public Enemy copy() {
        return new Enemy(getPosition(), getOrientation());
    }
}
