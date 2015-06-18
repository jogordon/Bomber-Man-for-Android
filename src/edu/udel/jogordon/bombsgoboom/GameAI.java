package edu.udel.jogordon.bombsgoboom;

import java.util.ArrayList;
import java.util.List;

import edu.udel.jogordon.gameframework.AIPlayer;
import edu.udel.jogordon.gameframework.Position;

public class GameAI extends AIPlayer<ChangeOrientationMove, GameState>{
	public GameAI() {
		super();
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return "AI";
	}

	@Override
	public List<ChangeOrientationMove> getAllValidMoves(GameState state) {
        List<ChangeOrientationMove> allMoves = new ArrayList<ChangeOrientationMove>();
        allMoves.add(new ChangeOrientationMove('U'));
        allMoves.add(new ChangeOrientationMove('D'));
        allMoves.add(new ChangeOrientationMove('L'));
        allMoves.add(new ChangeOrientationMove('R'));
        allMoves.add(new ChangeOrientationMove('C'));
        return allMoves;
	}

	@Override
	public double getHeuristicScore(GameState state) {
		PlayerState player = state.getPlayerState();
		Position playerPosition = player.getPosition();

		Position nextPosition = GameSprite.next(playerPosition, player.getOrientation());
        if (!state.isValid(nextPosition, this)) {
            // then it wouldn't end up moving anywhere, so just treat it as
            // if it would stay still
            nextPosition = playerPosition;
        }
        /*
         * Determine the nearest bomb, enemy, powerup, and breakable wall
         * and calculate their respective distances from the player
         */
        int bombDist = 1000;
		Bomb nearestB = null;
		for(Bomb b : state.getBombs()) {
			if(nextPosition.blockDistance(b.getPosition()) < bombDist) {
				bombDist = nextPosition.blockDistance(b.getPosition());
				nearestB = b;
			}
		}       
		int enemyDist = 1000;
		Enemy nearestE = null;
		for(Enemy e : state.getEnemies()) {
			if(nextPosition.blockDistance(e.getPosition()) < enemyDist) {
				enemyDist = nextPosition.blockDistance(e.getPosition());
				nearestE = e;
			}
		}
		int powerupDist = 1000;
		//Powerup nearestP = null;
		for(Powerup p : state.getPowerups()) {
			if(nextPosition.blockDistance(p.getPosition()) < powerupDist) {
				powerupDist = nextPosition.blockDistance(p.getPosition());
				//nearestP = p;
			}
		}
		int wallDist = 1000;
		//Wall nearestW = null;
		for(Wall w : state.getWalls()) {
			if((nextPosition.blockDistance(w.getPosition()) < wallDist) && w.getBreakable() == true) {
				wallDist = nextPosition.blockDistance(w.getPosition());
				//nearestW = w;
			}
		}

		//Calculate total heuristic score using various weightings
		double score1 =  (0.60*bombScore(nearestB, bombDist)) + (0.20*enemyScore(nearestE, enemyDist, nextPosition)) + 
				(0.08*pathScore(player, nextPosition)) + (0.10*powerupScore(powerupDist))
				+ (0.02*wallScore(wallDist));
		return score1;
	}
	public double bombScore(Bomb nearestB, int bombDist) {
		//Calculate bomb score, too close is bad, especially if bomb is upgraded
		if((nearestB != null) && (nearestB.upgrade == "Power2x")) {
			if(bombDist < 2 || (bombDist < 3 && (nearestB.getPosition().getRow() == nearestB.getPosition().getRow() || 
								nearestB.getPosition().getCol() == nearestB.getPosition().getCol()))) {
				 return 0;
			}
			else {return 10;}
		}
		else {
			if(bombDist < 2) {
				return 0;
			}
			else{return 10;}
		}
	}
	public double enemyScore(Enemy nearestE, int enemyDist, Position nextPosition) {
		//Calculate enemy score, get close enough to bomb, but not too close!
		if(enemyDist == 0) {return -100;}
		if(enemyDist == 1) {
			char enemyOrientation = nearestE.getOrientation();
			Position enemyPos = nearestE.getPosition();
			if((enemyOrientation == 'U') && (new Position(enemyPos.getRow()+1, enemyPos.getCol()).equals(nextPosition))) {
				return -100;
			}
			else if((enemyOrientation == 'D') && (new Position(enemyPos.getRow()-1, enemyPos.getCol()).equals(nextPosition))) {
				return -100;
			}
			else if((enemyOrientation == 'R') && (new Position(enemyPos.getRow(), enemyPos.getCol()+1).equals(nextPosition))) {
				return -100;
			}
			else if((enemyOrientation == 'L') && (new Position(enemyPos.getRow(), enemyPos.getCol()+1).equals(nextPosition))) {
				return -100;
			}
			else {return 10;}
		}
		else {return Math.max((11 - enemyDist), 0);}
	}
	public double pathScore(PlayerState player, Position nextPosition) {
		// PlayerState keeps track of last four previous positions, it is 
		// undesirable to return to locations you have recently been at
		int y = 1;
		if(nextPosition.equals(player.getPosition())) {return 0;}
		for(Position x : player.lastFour) {
			if (nextPosition.equals(x)) {return y*2;}
			y++;
		}
		return 10;
	}
	public double powerupScore(int powerupDist) {
		// Walk into blocks containing powerups to collect them
		return Math.max((10 - powerupDist), 0);
	}
	public double wallScore(int wallDist) {
		// Walking near breakable walls allows opportunity to destroy them with bombs
		if(wallDist == 0) {return 0;}
		return Math.max((11 - wallDist), 0);
	}
	 public void toBombOrNotToBomb(GameState state) {
	    	//This logic determines whether the player should move to the chosen cell or
	    	//place a bomb there
		PlayerState player = state.getPlayerState();
	    @SuppressWarnings("static-access")
		Position next = player.next(player.getPosition(), player.getOrientation());
	    int bombDist = 1000;
	    for(Bomb b : state.getBombs()) {
	    	if((next.blockDistance(b.getPosition()) < bombDist)) {
	    		bombDist = next.blockDistance(b.getPosition());
	    	}
	    }
	    if(state.isValid(next, this) && (bombDist > 4) && !enemyToClose(state)) {
	    	for(Enemy e : state.getEnemies()) {
	    		if((next.blockDistance(e.getPosition()) <= 2)) {
	    			state.getPlayerState().setBombDecision(true);    			
	    		}
	    	}
	    	for(Wall w : state.getWalls()) {
	    		if((next.blockDistance(w.getPosition()) == 1) && (w.getBreakable())) {
	    			state.getPlayerState().setBombDecision(true);
	    		}
	    	}
	    }
	    else {state.getPlayerState().setBombDecision(false);}
	 }
	 public boolean enemyToClose(GameState state) {
		 
		Position playerPosition = state.getPlayerState().getPosition();
		int enemyDist = 1000;
		Enemy nearestE = null;
		for(Enemy e : state.getEnemies()) {
			if(playerPosition.blockDistance(e.getPosition()) < enemyDist) {
				enemyDist = playerPosition.blockDistance(e.getPosition());
				nearestE = e;
			}
		}
		if(playerPosition.blockDistance(nearestE.getPosition()) == 1) {
			char enemyOrientation = nearestE.getOrientation();
			Position enemyPos = nearestE.getPosition();
			if(((enemyOrientation == 'U') && (new Position(enemyPos.getRow()+1, enemyPos.getCol()).equals(playerPosition))) ||
			   ((enemyOrientation == 'D') && (new Position(enemyPos.getRow()-1, enemyPos.getCol()).equals(playerPosition))) ||
			   ((enemyOrientation == 'R') && (new Position(enemyPos.getRow(), enemyPos.getCol()+1).equals(playerPosition))) ||
			   ((enemyOrientation == 'L') && (new Position(enemyPos.getRow(), enemyPos.getCol()+1).equals(playerPosition)))) {
				return true;
			}
		}
		return false;		 
	 }
}