package edu.udel.jogordon.bombsgoboom;

import java.util.ArrayDeque;
import java.util.Iterator;

import edu.udel.jogordon.gameframework.Position;

public class PlayerState extends GameSprite<PlayerState>{
	ArrayDeque<Position> lastFour; // These hold the last four positions the player was at
								   //  The AI uses this to avoid redundant moves that might 
								   // result in never finding a way around a wall
	String upgrade = "";
	boolean bombDecision = false;  //  Will the player drop a bomb?
	char lastOrientation;		   // Saves the previous non 'C' orientation
	
	public PlayerState(Position position, char orientation, ArrayDeque<Position> lastFour) {
        super(position, orientation);
        this.lastFour = lastFour;
    }

    public void onTick(GameState state) {
    	upgrade = upgrade(state);
    	if(bombDecision) {
    		if(getOrientation() == 'C') {
    			bombsAway(state, lastOrientation);    			
    		}
    		else {bombsAway(state, getOrientation());}
   			bombDecision = false;
    	}
    	else {move(getOrientation(), state);}
    }
    public ArrayDeque<Position> getLastFour() {
    	return lastFour;
    }
    public void setLastFour(ArrayDeque<Position> lastFour) {
    	this.lastFour = lastFour;
    }
    public boolean getBombDecision() {
    	return bombDecision;
    }
    public void setBombDecision(boolean bombDecision) {
    	this.bombDecision = bombDecision;
    }
    public char getLastOrientation() {
    	return lastOrientation;
    }
    public void setLastOrientation(char lastOrientation) {
    	this.lastOrientation = lastOrientation;
    }
    public String getUpgrade() {
    	return upgrade;
    }
    public void setUpgrade(String upgrade) {
    	this.upgrade = upgrade;
    }
    public String upgrade(GameState state) {
    	//If walk over a upgrade, gain the ability to plant more powerful bombs
    	//Remove the upgrade object from the game
        Iterator<Powerup> powerupIterator = state.getPowerups().iterator();
    	while(powerupIterator.hasNext()) {
    		Powerup nextPowerup = powerupIterator.next();
    		if(this.getPosition().equals(nextPowerup.getPosition())) {
    			String power = nextPowerup.getUpgrade();
    			powerupIterator.remove();
    			return power;
    		}
    	}
    	return upgrade;
    }
    public void bombsAway(GameState state, char orientation) {
    	//Deploys a bomb
    	Position next = next(getPosition(), orientation);
    	Bomb bomb = new Bomb(next, getOrientation(), upgrade, 3);
    	state.getBombs().addLast(bomb);
    	bombDecision = false;
    }
    public PlayerState copy() {
        return new PlayerState(getPosition(), getOrientation(), lastFour);
    }
    public String toString() {
    	String attributes = "Position: "+ getPosition() + ", Orientation: "+ 
    						getOrientation() + ", LastFour:"; 
    	for(Position p : lastFour) {
    		attributes += p;
    	}
    	return attributes;
    }

}
