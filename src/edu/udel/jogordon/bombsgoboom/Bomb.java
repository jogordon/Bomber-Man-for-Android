package edu.udel.jogordon.bombsgoboom;
import java.util.ArrayDeque;
import java.util.Iterator;

import edu.udel.jogordon.gameframework.Position;

public class Bomb extends GameSprite<Bomb>{
	String upgrade;								//  How powerful is the bomb
	int timer;									// How much time left until the bomb explodes

	public Bomb(Position position, char orientation, String upgrade, int timer) {
        super(position, orientation);
        this.upgrade = upgrade;
        this.timer = timer;
    }
	
	public String getUpgrade() {return upgrade;}
	
	public void setUpgrade(String upgrade) {
		this.upgrade = upgrade;
	}
	
	public int getTimer() {return timer;}
	
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
    public void onTick(GameState state) {
    	//Check to see if any bombs have blown up
      	setTimer(timer-1);
    	Iterator<Bomb> bombIterator = state.getBombs().iterator();
    	while(bombIterator.hasNext()) {
    		Bomb nextBomb = bombIterator.next();
    		if(nextBomb.timer == 0) {
    			Position p = nextBomb.getPosition();
    			blownUp(state, p, nextBomb.getUpgrade()); // Call blownUp() which will delete affected elements
    			ArrayDeque<Explosion> explosionArray = state.getExplosions();
    			explosionArray.add(new Explosion(getPosition(), 'C', nextBomb.getUpgrade(), 1));
    			explosionArray.add(new Explosion(new Position(getPosition().getRow()-1, getPosition().getCol()), 'U', nextBomb.getUpgrade(), 1));
    			explosionArray.add(new Explosion(new Position(getPosition().getRow(), getPosition().getCol()+1), 'R', nextBomb.getUpgrade(), 1));
    			explosionArray.add(new Explosion(new Position(getPosition().getRow()+1, getPosition().getCol()), 'D', nextBomb.getUpgrade(), 1));
    			explosionArray.add(new Explosion(new Position(getPosition().getRow(), getPosition().getCol()-1), 'L', nextBomb.getUpgrade(), 1));
    			if(nextBomb.getUpgrade() == "Power2x") {
    				explosionArray.add(new Explosion(new Position(getPosition().getRow()-2, getPosition().getCol()), 'U', nextBomb.getUpgrade(), 1));
        			explosionArray.add(new Explosion(new Position(getPosition().getRow(), getPosition().getCol()+2), 'R', nextBomb.getUpgrade(), 1));
        			explosionArray.add(new Explosion(new Position(getPosition().getRow()+2, getPosition().getCol()), 'D', nextBomb.getUpgrade(), 1));
        			explosionArray.add(new Explosion(new Position(getPosition().getRow(), getPosition().getCol()-2), 'L', nextBomb.getUpgrade(), 1));
    			}
    		}
			if(nextBomb.timer == -1) {bombIterator.remove();}
    	}
    }
    public void blownUp(GameState state, Position p, String upgrade) {
    	//Determines if walls or enemies were blown up by an exploding bomb
    	//If so, remove them from the game
    	Iterator<Wall> wallIterator = state.getWalls().iterator();
    	while(wallIterator.hasNext()) {
    		Wall nextWall = wallIterator.next();
   			if(nextWall.getBreakable() && ((p.blockDistance(nextWall.getPosition()) == 1) || 
   				((upgrade == "Power2x") && (p.blockDistance(nextWall.getPosition()) <= 2) && 
   				(p.getRow() == nextWall.getPosition().getRow() || 
   				p.getCol() == nextWall.getPosition().getCol())))) {
   					wallIterator.remove();
    		}
    	}
    	int enemyPop1 = state.getEnemies().size();
    	Iterator<Enemy> enemyIterator = state.getEnemies().iterator();
    	while(enemyIterator.hasNext()) {
    		Enemy nextEnemy = enemyIterator.next();
    		if((p.blockDistance(nextEnemy.getPosition()) == 1) || 
       		  ((upgrade == "Power2x") && (p.blockDistance(nextEnemy.getPosition()) <= 2) && 
       		   (p.getRow() == nextEnemy.getPosition().getRow() || 
       		    p.getCol() == nextEnemy.getPosition().getCol()))) {
    			enemyIterator.remove();
    		}
    	}
    	//Award points if any enemies were killed
    	if ((enemyPop1 - state.getEnemies().size()) == 1)  {
    		state.setScore(state.getScore() + 10);
    	}
    	//Bonus points if more than one enemy was killed 
    	else if ((enemyPop1 - state.getEnemies().size()) > 1) {
    		state.setScore(state.getScore() + (int)((Math.pow(enemyPop1 - state.getEnemies().size(),2))*10)); 	
    	}
    }

    public Bomb copy() {
        return new Bomb(getPosition(), getOrientation(), upgrade, timer);
    }
    public String toString() {
    	return "Position: "+ getPosition() + ", Orientation: "+ getOrientation() + 
    		   ", Upgrade: " + upgrade + ", Timer: " + timer + "\n";
    }
}
