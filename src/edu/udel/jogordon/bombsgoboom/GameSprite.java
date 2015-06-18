package edu.udel.jogordon.bombsgoboom;

import java.util.ArrayList;
import java.util.Arrays;
import edu.udel.jogordon.gameframework.Position;

public abstract class GameSprite<T extends GameSprite<T>> {

	private Position position;					//All sprites have a position
	private char orientation;					// All sprites have an orientation
	
	protected GameSprite(Position position, char orientation) {
        this.position = position;
        this.orientation = orientation;
    }
    
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    
    public abstract T copy();
    
    public void onTick(GameState state) {
        
    }
    
    protected void move(char orientation, GameState state) {
        Position next = next(getPosition(), orientation);
        if (state.isValid(next, this)) {
            setPosition(next);
            if(this instanceof PlayerState) {
            	// PlayerState keeps track of recently visited positions
            	// AI returning to recently visited positions is undesirable 
            	PlayerState y = (PlayerState)this;
            	y.getLastFour().addLast(next);
            	if(y.getLastFour().size() > 4) {
            		y.getLastFour().removeFirst();
            	}
            }
        }
        else {//If enemies have chosen an invalid position, they will be redirected to 
        	  //a valid move with this loop
        	ArrayList<Character> chars = new ArrayList<Character>(Arrays.asList('U','D','L','R','C'));
        	chars.remove(chars.indexOf(orientation));
        	while(!state.isValid(next, this)) {
        		int randIndex = (int)(Math.random()*chars.size());
           		char newOrientation = chars.get(randIndex);
           		this.setOrientation(newOrientation);
        		next = next(getPosition(), newOrientation);
        		chars.remove(chars.indexOf(newOrientation));
            }
        	setPosition(next);
        }
    }
    
	public char getOrientation() {
		return orientation;
	}

	public void setOrientation(char orientation) {
		this.orientation = orientation;
	}
	public static Position next(Position p, char orientation) {
	      if (orientation == 'U') {
	          return new Position(p.getRow()-1, p.getCol());
	      }
	      else if (orientation == 'D') {
	          return new Position(p.getRow()+1, p.getCol());
	      }
	      else if (orientation == 'L') {
	          return new Position(p.getRow(), p.getCol()-1);
	      }
	      else if (orientation == 'R') {
	          return new Position(p.getRow(), p.getCol()+1);
	      }
	      else { // This includes the 'C' condition
	          return p;
	      }
	}
}
