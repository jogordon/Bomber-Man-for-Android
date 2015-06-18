package edu.udel.jogordon.bombsgoboom;

import edu.udel.jogordon.gameframework.Move;
import edu.udel.jogordon.gameframework.Position;

public class ChangeOrientationMove implements Move<GameState> {
	private char orientation;
	
	public ChangeOrientationMove(char orientation) {
		this.orientation = orientation;
	}
	
	public char getOrientation() {
		return orientation;
	}

	@SuppressWarnings("static-access")
	public boolean isValid(GameState state) {//Determine if the players move is valid
		PlayerState player = state.getPlayerState();
		Position next = player.next(player.getPosition(), orientation);
		return state.isValid(next, this);
	}

	public void make(GameState state) {
		state.getPlayerState().setOrientation(orientation);		
	}
}
