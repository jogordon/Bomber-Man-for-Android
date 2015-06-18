package edu.udel.jogordon.bombsgoboom.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import edu.udel.jogordon.gameframework.Player;
import edu.udel.jogordon.gameframework.android.AndroidTickManager;
import edu.udel.jogordon.bombsgoboom.GameAI;
import edu.udel.jogordon.bombsgoboom.BombsGoBoom;
import edu.udel.jogordon.bombsgoboom.ChangeOrientationMove;
import edu.udel.jogordon.bombsgoboom.GameRecord;
import edu.udel.jogordon.bombsgoboom.GameState;

public class GameActivity extends Activity {
    
    private GameView gameView;
    private Scoreboard scoreboard;
    private GameSoundManager soundManager;
    private BombsGoBoom game;
    private MoveListener moveListener;
    
    private GameDatabase database;
    private EndOfGameDialog dialog;
    private GameRecord highScoreRecord;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initGUI();
        database = new GameDatabase(this);
        setHighScoreRecord(database.getHighestScoreGameRecord());
        soundManager = new GameSoundManager(this);
        soundManager.init();
        
        game = newOnePlayerGame();
        startGame(game);
    }
    public GameDatabase getDatabase() {
        return database;
    }
    
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private void initGUI() {
        gameView = new GameView(this);
        scoreboard = new Scoreboard(this);
        
        moveListener = new MoveListener(this);
        gameView.setOnTouchListener(moveListener);
        gameView.setOnKeyListener(moveListener);
        
        LinearLayout withScoreboard = new LinearLayout(this);
        withScoreboard.setOrientation(LinearLayout.VERTICAL);
        withScoreboard.addView(scoreboard);
        withScoreboard.addView(gameView);
        
        dialog = new EndOfGameDialog(this);
        
        setContentView(withScoreboard);        
    }

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add("Demo");
        menu.add("1 Player Game");
        menu.add("Restart");
        menu.add("Quit");
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        CharSequence title = item.getTitle();
        if (title.equals("Demo")) {
            game = newAIGame();
            startGame(game);
        }
        else if (title.equals("1 Player Game")) {
            game = newOnePlayerGame();
            startGame(game);
        }
        else if (title.equals("Restart")) {
            // start a new game with the same players as previous game
            restartGame();
        }
        else if (title.equals("Quit")) {
            finish();
        }
        return true;
    }
    public GameRecord getHighScoreRecord() {
        return highScoreRecord;
    }

    public void setHighScoreRecord(GameRecord highScoreRecord) {
        this.highScoreRecord = highScoreRecord;
    }
    
    public BombsGoBoom getCurrentGame() {
        return game;
    }
    
    private void startGame(BombsGoBoom game) {
        game.addStateChangeListener(gameView);
        game.addStateChangeListener(scoreboard);
        game.addStateChangeListener(dialog);
        game.addStateChangeListener(soundManager);
        HumanPlayer human = new HumanPlayer(moveListener); 
        game.addPlayer(human);
        game.start();
        AndroidTickManager.manageGame(game);
    }
    
    public void restartGame() {
        BombsGoBoom newGame = new BombsGoBoom();
        for (Player<ChangeOrientationMove, GameState> p : game.getPlayers()) {
            newGame.addPlayer(p);
        }
        game = newGame;
        startGame(game);
    }
    
    private BombsGoBoom newOnePlayerGame() {
        BombsGoBoom game = new BombsGoBoom();
        HumanPlayer human = new HumanPlayer(moveListener);
        game.addPlayer(human);
        return game;
    }
    
    private BombsGoBoom newAIGame() {
        BombsGoBoom game = new BombsGoBoom();
        GameAI ai = new GameAI();
        game.addPlayer(ai);
        return game;
    }
}