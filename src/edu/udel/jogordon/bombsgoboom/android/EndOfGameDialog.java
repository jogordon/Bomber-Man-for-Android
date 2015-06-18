package edu.udel.jogordon.bombsgoboom.android;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import edu.udel.jogordon.bombsgoboom.GameRecord;
import edu.udel.jogordon.bombsgoboom.GameState;
import edu.udel.jogordon.gameframework.AIPlayer;
import edu.udel.jogordon.gameframework.GameStateListener;

public class EndOfGameDialog implements GameStateListener, DialogInterface.OnClickListener {
    private GameActivity activity;
    private EditText input;
    private GameRecord lastRecord;

    public EndOfGameDialog(GameActivity activity) {
        this.activity = activity;
    }
    
    public void onClick(DialogInterface dialog, int whichButton) {
        // format value to only valid letters, and only the first 3, uppercase
    	if(this.input != null) {
    		String value = String.format("%3s", 
    			this.input.getText().toString().replaceAll("[^a-zA-Z]", "")).substring(0,3).toUpperCase();

    		// get the score integer to store in database
    		Integer score = activity.getCurrentGame().getCurrentState().getScore();

    		// create the record
    		lastRecord = new GameRecord(value, score, System.currentTimeMillis());
        
    		// insert record to database
    		activity.getDatabase().insertGameRecord(lastRecord);
        
    		activity.setHighScoreRecord(lastRecord);
    		// force the scoreboard/view to update
    		activity.getScoreboard().onStateChange(activity.getCurrentGame());
    		}
    }
    
    /**
     * Gets the last initials entered.  Will return "" if none entered yet.
     * 
     * @return
     */
    public String getLastInitials() {
        String lastInitials = "";
        if (this.lastRecord != null) {
            lastInitials = lastRecord.getPlayer();
        }
        return lastInitials;
    }

    
    public void onStateChange(Object game) {
        if (game == activity.getCurrentGame()) {
            GameState state = activity.getCurrentGame().getCurrentState();
            // don't let the AI get the high score at snake
            if (state.isEnd() && !activity.isFinishing() 
                    && !(activity.getCurrentGame().getPlayers().get(0) instanceof AIPlayer)) {
                int score = activity.getCurrentGame().getCurrentState().getScore();
                ArrayList<GameRecord> topScores = activity.getDatabase().getFiveHighestScoreRecords();
                if(topScores.size() > 0) {
                	GameRecord lastRecord = topScores.get(topScores.size()-1);
                }
                else {GameRecord lastRecord = null;}
                if (lastRecord == null || score > lastRecord.getScore()) {
                    // pop up a dialog box telling them to enter name
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
    
                    alert.setTitle("Your Score of " + score + " is in the TOP FIVE!");
                    String playerList = "";
                	int rank = 1;
                	for(GameRecord r : topScores) {
                		playerList += rank +".\t\t\t" + r.getPlayer() + "\t\t\t" +
                    			r.getScore() + "\t\t\t" + new Date(r.getDate()) + "\n";
                		rank++;
                	}
                    alert.setMessage("Rank \t" + "Player\t\t" + "Score\t\t" + "Date\n" + playerList
                    		+"\n Please enter your initials:");
                    
                    // get these so we can prompt them with the last initials to start
                    // (makes it easy to play multiple games rather than having them retype
                    //  their initials every time)
                    String lastInitials = getLastInitials();
                    this.input = new EditText(activity);
                    this.input.setText(lastInitials);
                        
                    // Set the view to the EditText 
                    alert.setView(this.input);
                    alert.setPositiveButton("Ok", this);
                    
                    alert.show();
                }
                else {
                	AlertDialog.Builder topFive = new AlertDialog.Builder(activity);
                
                	topFive.setTitle("BombsGoBoom! All-Stars!");
                	String playerList = "";
                	int rank = 1;
                	for(GameRecord record : activity.getDatabase().getFiveHighestScoreRecords()) {
                		playerList += rank +".\t\t\t" + record.getPlayer() + "\t\t\t" +
                    			record.getScore() + "\t\t\t" + new Date(record.getDate()) + "\n";
                		rank++;
                	}
                	topFive.setMessage("Rank \t" + "Player\t\t" + "Score\t\t" + "Date\n" +
                			playerList);
                	topFive.setPositiveButton("Close", this);
                	topFive.show();
                }
            }
        }
        
    }
}
