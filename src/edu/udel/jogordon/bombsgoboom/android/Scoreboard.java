package edu.udel.jogordon.bombsgoboom.android;

import android.widget.TextView;
import android.widget.Toast;
import edu.udel.jogordon.gameframework.GameStateListener;
import edu.udel.jogordon.bombsgoboom.GameState;

public class Scoreboard extends TextView implements GameStateListener {
    private int level;
    private int score;
    private GameActivity activity;
    
    public Scoreboard(GameActivity context) {
        super(context);
        activity = context;
    }

    public void onStateChange(Object game) {
        if (game == activity.getCurrentGame()) {
            GameState state = activity.getCurrentGame().getCurrentState();

            String message = "Score = " + state.getScore() + " Level = " + state.getLevel() + " Time Elapsed = " + state.getGameTimer().getTimeElapsed();
            if (state.isEnd()) {
            	if(state.getEnemies().isEmpty()) {
            		if(state.getLevel() == 3) {
            			message = "Congrats, you beat the game! " + message;
            		}
            		else {
            			message = "Level Complete! " + message;
            		}
            	}
            	else {
            		message = "You have died. Such is fate... "  + message;
            		
            	}
            }
            setText(message);
    
            int newPoints = state.getScore() - score;
            if (newPoints == 10) {
            	Toast.makeText(getContext(), "+10 points!", Toast.LENGTH_SHORT).show();
            }
            else if((newPoints > 10) && (level == state.getLevel())) {
            	Toast.makeText(getContext(), "COMBO KILL! +" +newPoints+ " points!", Toast.LENGTH_SHORT).show();
            }
            else if((newPoints != 0) && (level != state.getLevel())) {
            	Toast.makeText(getContext(), "KILL + QUICK FINISH BONUS! +" + newPoints + " points!", Toast.LENGTH_SHORT).show();
            }
            score = state.getScore();
            if (level != state.getLevel()) {
                level = state.getLevel();
                if (level > 1) {
                    Toast.makeText(getContext(), "Level " + level + "!", Toast.LENGTH_SHORT).show();
                }
            }
            	
        }
    }

}
