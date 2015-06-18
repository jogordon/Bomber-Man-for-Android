package edu.udel.jogordon.bombsgoboom.android;

import edu.udel.jogordon.gameframework.Player;
import edu.udel.jogordon.gameframework.Position;
import edu.udel.jogordon.bombsgoboom.GameState;
import edu.udel.jogordon.bombsgoboom.ChangeOrientationMove;

public class HumanPlayer implements Player<ChangeOrientationMove, GameState> {
    private MoveListener listener;
    private Position target = null;
    private Position bombTarget = null;
    boolean bombSignal;

    public HumanPlayer(MoveListener listener) {
        this.listener = listener;
    }

    public ChangeOrientationMove getNextMove(GameState state) {
        Position moveTo = listener.getMoveToPosition();     
        if (moveTo != null) {
        	target = moveTo;
        	}
        if (target != null) {
            Position playerPosition = state.getPlayerState().getPosition();
            int changeX = target.getCol() - playerPosition.getCol();
            int changeY = target.getRow() - playerPosition.getRow();
            char orientation = state.getPlayerState().getOrientation();
            if ((changeY == 0) && (changeX == 0)) {
            	orientation = 'C';
            }
            if (changeY == changeX) {
            	if(changeY > 0) {
            	int randomInt = (int)(Math.random()*2);
            	if (randomInt == 0 ) {orientation = 'D';}
            	else {orientation = 'R';}
            	}
            	else if(changeY < 0) {
            		int randomInt = (int)(Math.random()*2);
            		if (randomInt == 0 ) {orientation = 'U';}
            		else {orientation = 'L';}            	
            	}
            }
            else if(Math.abs(changeY) == Math.abs(changeX)) {
            	if ((changeY > 0) && (changeX < 0)) {
            		int randomInt = (int)(Math.random()*2);
            		if (randomInt == 0 ) {orientation = 'D';}
            		else {orientation = 'L';}             	
            	}
            	else if((changeY < 0) && (changeX > 0)) {
            		int randomInt = (int)(Math.random()*2);
            		if (randomInt == 0 ) {orientation = 'U';}
            		else {orientation = 'R';}  
            	}
            }
            else if((Math.abs(changeY) > Math.abs(changeX)) && changeY > 0) {orientation = 'D';}
            else if((Math.abs(changeY) > Math.abs(changeX)) && changeY < 0) {orientation = 'U';}
            else if((Math.abs(changeY) < Math.abs(changeX)) && changeX > 0) {orientation = 'R';}
            else if((Math.abs(changeY) < Math.abs(changeX)) && changeX < 0) {orientation = 'L';}
            return new ChangeOrientationMove(orientation);
        }
        return new ChangeOrientationMove(state.getPlayerState().getOrientation());
    }
    
    public String getIdentifier() {
        return "Human";
    }

    public String toString() {
        return getIdentifier();
    }

	@Override
	public void toBombOrNotToBomb(GameState state) {
		Position moveTo = listener.getMoveToPosition();
		if(moveTo != null && bombTarget != null && listener.isDoubleClick()) {
			if (bombTarget.equals(moveTo)) {
				bombSignal = true;
			}
			else {
				bombSignal = false;
			}
		}
		if(moveTo != null) {bombTarget = moveTo;}
		if(bombSignal == true) {
			Position playerPosition = state.getPlayerState().getPosition();
			int playerRow = playerPosition.getRow();
			int playerCol = playerPosition.getCol();
			int targetRow = bombTarget.getRow();
			int targetCol = bombTarget.getCol();

			if((state.getPlayerState().getOrientation() == 'U') && (targetRow - playerRow == -1) &&
				(targetCol - playerCol == 0)) {
				state.getPlayerState().setBombDecision(true);
				bombSignal = false;
				target = playerPosition;
				state.getPlayerState().setLastOrientation('U');
			}
			else if((state.getPlayerState().getOrientation() == 'D') && (targetRow - playerRow == 1) &&
				(targetCol - playerCol == 0)) {
				state.getPlayerState().setBombDecision(true);
				bombSignal = false;
				target = playerPosition;
				state.getPlayerState().setLastOrientation('D');
			}
			else if((state.getPlayerState().getOrientation() == 'R') && (targetRow - playerRow == 0) &&
				(targetCol - playerCol == 1)) {
				state.getPlayerState().setBombDecision(true);
				bombSignal = false;
				target = playerPosition;
				state.getPlayerState().setLastOrientation('R');
			}
			else if((state.getPlayerState().getOrientation() == 'L') && (targetRow - playerRow == 0) &&
				(targetCol - playerCol == -1)) {
				state.getPlayerState().setBombDecision(true);
				bombSignal = false;
				target = playerPosition;
				state.getPlayerState().setLastOrientation('L');
			}
			else if((state.getPlayerState().getOrientation() == 'C') && (bombTarget.blockDistance(playerPosition) == 1)) {
				state.getPlayerState().setBombDecision(true);
				bombSignal = false;
				target = playerPosition;
			}
			else {state.getPlayerState().setBombDecision(false);}
		}
	}
}
