package edu.udel.jogordon.bombsgoboom;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;

import edu.udel.jogordon.gameframework.ConsoleTickManager;
import edu.udel.jogordon.gameframework.Game;
import edu.udel.jogordon.gameframework.Position;


public class BombsGoBoom extends Game<ChangeOrientationMove, GameState> {
	
public BombsGoBoom() {
	this(new GameState(
    			new PlayerState(new Position(0, 0), 'C', new ArrayDeque<Position>(
        			Arrays.asList(new Position(0, 0)))), 
        		new ArrayDeque<Enemy>(Arrays.asList(new Enemy(new Position(0, 5), 'L'), 
        			new Enemy(new Position(1, 13),'D'), new Enemy(new Position(6, 7), 'L'), 
        			new Enemy(new Position(8, 1),'D'), new Enemy(new Position(8, 12), 'R'))),
        		new ArrayDeque<Wall>(Arrays.asList(new Wall(new Position(0, 2), 'D', true),
        			new Wall(new Position(1, 2), 'D', true), new Wall(new Position(1, 6), 'R', false),
        			new Wall(new Position(1, 7), 'R', false), new Wall(new Position(2, 2), 'D', false),
        			new Wall(new Position(2, 3), 'R', false), new Wall(new Position(2, 7), 'R', false),
        			new Wall(new Position(2, 8), 'D', true), new Wall(new Position(2, 11), 'R', true),
        			new Wall(new Position(3, 3), 'D', false), new Wall(new Position(3, 4), 'R', false),
        			new Wall(new Position(3, 8), 'D', false), new Wall(new Position(3, 11), 'R', true),
        			new Wall(new Position(3, 12), 'D', true), new Wall(new Position(4, 5), 'R', true),
        			new Wall(new Position(4, 6), 'D', true), new Wall(new Position(4, 7), 'R', true),
        			new Wall(new Position(6, 0), 'D', true), new Wall(new Position(6, 1), 'R', true),
        			new Wall(new Position(6, 2), 'D', false), new Wall(new Position(6, 10), 'R', false),
        			new Wall(new Position(6, 11), 'D', false), new Wall(new Position(6, 12), 'R', false),
        			new Wall(new Position(7, 2), 'D', false), new Wall(new Position(7, 3), 'R', false),
        			new Wall(new Position(7, 4), 'D', false), new Wall(new Position(7, 9), 'R', false),
        			new Wall(new Position(7, 10), 'D', false), new Wall(new Position(8, 8), 'R', true),
        			new Wall(new Position(9, 8), 'D', true))), 
        		new ArrayDeque<Powerup>(Arrays.asList(new Powerup(new Position(2, 6), 'R', "Power2x"),
        			new Powerup(new Position(9, 10), 'R', "Power2x"))),
        		new ArrayDeque<Bomb>(), new ArrayDeque<Explosion>(), new GameTimer(), 0, 1, 10, 15));
    }

    public BombsGoBoom(GameState currentState) {
        super(currentState);
    }


    public void onTick() {
        super.onTick();
        
        GameState currentState = getCurrentState();
        // Tick enemies
        for(Enemy e : currentState.getEnemies()) {
        	e.onTick(currentState);
        }
        // Tick bombs
        for(Bomb b : currentState.getBombs()) {
        	b.onTick(currentState);
        }
        // Remove expired explosions, tick the rest
        Iterator<Explosion> explosionIterator = currentState.getExplosions().iterator();
    	while(explosionIterator.hasNext()) {
    		Explosion nextExplosion = explosionIterator.next();
    		if(nextExplosion.getTimer() <= 0) {
    			explosionIterator.remove();
    		}
    		else {nextExplosion.onTick(currentState);}
    	}
    	// Tick the playerState
        currentState.getPlayerState().onTick(currentState);
        if(currentState.getEnemies().isEmpty()) { 
        	//If level is complete and was finished fast, award bonus points
        	currentState.setScore((int)(currentState.getScore()*(1.00 + 
        			(1.00 - Math.min(1.00, (double)currentState.getGameTimer().getTimeElapsed() / 
        			(double)(currentState.getLevel()*200))))));
        	if (currentState.getLevel() < 3) {
        		nextLevel();
        	}
        }
        notifyStateChangeListeners();
    }
    public void nextLevel() {
       GameState state = getCurrentState();
        // Clear out the old level stuff
        PlayerState player = state.getPlayerState();
        player.lastFour.clear();
        player.setUpgrade("");
        player.setBombDecision(false);
        state.getBombs().clear();
        state.getEnemies().clear();
        state.getWalls().clear();
        state.getPowerups().clear();
        state.getExplosions().clear();
        state.setLevel(state.getLevel() + 1);
        if(state.getLevel() == 2) {//Set the new level
        	setLevelTwo();
        }
        else if(state.getLevel() == 3) {// Set the new level
        	setLevelThree();
        }
    }	
    public void setLevelTwo() {
    	GameState state = getCurrentState();
    	state.getPlayerState().setPosition(new Position(0, 0));
    	state.getPlayerState().setOrientation('C');
    	ArrayDeque<Wall> wallList = new ArrayDeque<Wall>(Arrays.asList(new Wall(new Position(0, 6), 'D', true),
    			new Wall(new Position(0, 11), 'D', false), new Wall(new Position(1, 1), 'D', false),
    			new Wall(new Position(1, 2), 'D', false), new Wall(new Position(1, 3), 'D', false),
    			new Wall(new Position(1, 6), 'D', false), new Wall(new Position(1, 8), 'D', false),
    			new Wall(new Position(1, 9), 'D', false), new Wall(new Position(1, 11), 'D', false),
    			new Wall(new Position(2, 1), 'D', false), new Wall(new Position(2, 5), 'D', false),
    			new Wall(new Position(2, 6), 'D', false), new Wall(new Position(2, 8), 'D', false),
    			new Wall(new Position(2, 11), 'D', false), new Wall(new Position(2, 12), 'D', false),
    			new Wall(new Position(2, 14), 'D', true), new Wall(new Position(3, 3), 'D', true),
    			new Wall(new Position(3, 8), 'D', false), new Wall(new Position(3, 14), 'D', true),
    			new Wall(new Position(4, 0), 'D', false), new Wall(new Position(4, 2), 'D', true),
    			new Wall(new Position(4, 3), 'D', true), new Wall(new Position(4, 10), 'D', true),
    			new Wall(new Position(4, 11), 'D', true), new Wall(new Position(4, 14), 'D', true),
    			new Wall(new Position(5, 0), 'D', false), new Wall(new Position(5, 5), 'D', false),
    			new Wall(new Position(5, 6), 'D', false), new Wall(new Position(5, 7), 'D', false),
    			new Wall(new Position(5, 8), 'D', false), new Wall(new Position(5, 10), 'D', true),
    			new Wall(new Position(6, 0), 'D', true), new Wall(new Position(6, 1), 'D', true),
    			new Wall(new Position(6, 7), 'D', false), new Wall(new Position(6, 12), 'D', false),
    			new Wall(new Position(6, 13), 'D', false), new Wall(new Position(6, 14), 'D', false),
    			new Wall(new Position(7, 3), 'D', true), new Wall(new Position(7, 5), 'D', false),
    			new Wall(new Position(7, 9), 'D', true), new Wall(new Position(7, 14), 'D', false),
    			new Wall(new Position(8, 1), 'D', false), new Wall(new Position(8, 2), 'D', false),
    			new Wall(new Position(8, 3), 'D', true), new Wall(new Position(8, 5), 'D', false),
    			new Wall(new Position(8, 6), 'D', false), new Wall(new Position(8, 8), 'D', true),
    			new Wall(new Position(8, 9), 'D', true), new Wall(new Position(8, 11), 'D', false),
    			new Wall(new Position(9, 3), 'D', true), new Wall(new Position(9, 11), 'D', false)));
    	
    	state.setWalls(wallList);
    	
    	ArrayDeque<Enemy> enemyList = new ArrayDeque<Enemy>(Arrays.asList(new Enemy(new Position(0, 9), 'R'), new Enemy(new Position(1, 4), 'D'),
    			new Enemy(new Position(1, 13), 'D'), new Enemy(new Position(4, 6), 'R'),
    			new Enemy(new Position(4, 12), 'R'), new Enemy(new Position(6, 3), 'L'),
    			new Enemy(new Position(8, 13), 'U'), new Enemy(new Position(9, 7), 'L')));
    	
    	state.setEnemies(enemyList);
    	state.getPowerups().add(new Powerup(new Position(0, 13), 'D', "Power2x"));
    	state.getPowerups().add(new Powerup(new Position(9, 13), 'D', "Power2x"));
    	state.getGameTimer().setStartTime(System.currentTimeMillis());
    }
    public void setLevelThree() {
    	GameState state = getCurrentState();
    	state.getPlayerState().setPosition(new Position(0, 0));
    	state.getPlayerState().setOrientation('C');
    	ArrayDeque<Wall> wallList = new ArrayDeque<Wall>(Arrays.asList(new Wall(new Position(0, 1), 'D', false),
    			new Wall(new Position(0, 3), 'D', false), new Wall(new Position(0, 5), 'D', false),
    			new Wall(new Position(0, 9), 'D', false), new Wall(new Position(1, 2), 'D', false),
    			new Wall(new Position(1, 6), 'D', false), new Wall(new Position(1, 8), 'D', false),
    			new Wall(new Position(1, 10), 'D', false), new Wall(new Position(1, 13), 'D', false),
    			new Wall(new Position(2, 1), 'D', false), new Wall(new Position(2, 4), 'D', false),
    			new Wall(new Position(2, 11), 'D', false), new Wall(new Position(2, 14), 'D', false),
    			new Wall(new Position(3, 2), 'D', false), new Wall(new Position(3, 5), 'D', false),
    			new Wall(new Position(3, 7), 'D', true), new Wall(new Position(3, 13), 'D', false),
    			new Wall(new Position(4, 0), 'D', false), new Wall(new Position(4, 3), 'D', false),
    			new Wall(new Position(4, 6), 'D', false), new Wall(new Position(4, 8), 'D', false),
    			new Wall(new Position(4, 10), 'D', false), new Wall(new Position(5, 1), 'D', false),
    			new Wall(new Position(5, 5), 'D', true), new Wall(new Position(5, 9), 'D', true),
    			new Wall(new Position(5, 11), 'D', false), new Wall(new Position(5, 13), 'D', false),
    			new Wall(new Position(6, 0), 'D', false), new Wall(new Position(6, 2), 'D', false),
    			new Wall(new Position(6, 4), 'D', false), new Wall(new Position(6, 6), 'D', false),
    			new Wall(new Position(6, 8), 'D', false), new Wall(new Position(6, 12), 'D', false),
    			new Wall(new Position(7, 5), 'D', false), new Wall(new Position(7, 7), 'D', true),
    			new Wall(new Position(7, 10), 'D', false), new Wall(new Position(8, 1), 'D', true),
    			new Wall(new Position(8, 4), 'D', false), new Wall(new Position(8, 9), 'D', false),
    			new Wall(new Position(8, 12), 'D', false), new Wall(new Position(8, 14), 'D', false),
    			new Wall(new Position(9, 0), 'D', false), new Wall(new Position(9, 2), 'D', false),
    			new Wall(new Position(9, 5), 'D', false), new Wall(new Position(9, 8), 'D', false),
    			new Wall(new Position(9, 10), 'D', false), new Wall(new Position(9, 13), 'D', false)));
    	
    	state.setWalls(wallList);
     	
    	ArrayDeque<Enemy> enemyList = new ArrayDeque<Enemy>(Arrays.asList(new Enemy(new Position(0, 7), 'D'), new Enemy(new Position(0, 14), 'L'),
    			new Enemy(new Position(2, 2), 'R'), new Enemy(new Position(3, 9), 'R'),
    			new Enemy(new Position(5, 6), 'R'), /*new Enemy(new Position(5, 8), 'L'),*/
    			new Enemy(new Position(7, 0), 'R'), new Enemy(new Position(7, 0), 'L'),
    			new Enemy(new Position(9, 6), 'U')));
     	
    	state.setEnemies(enemyList);
     	
    	state.getPowerups().add(new Powerup(new Position(5, 7), 'D', "Power2x"));
    	state.getGameTimer().setStartTime(System.currentTimeMillis());
    }
    public String toString() {
	        return getCurrentState().toString();
	    }
	    
    public static void main(String[] args) {
    	       
	    BombsGoBoom game = new BombsGoBoom();
	    game.addPlayer(new GameAI());
	    ConsoleTickManager.manageGame(game);
	    }
}
