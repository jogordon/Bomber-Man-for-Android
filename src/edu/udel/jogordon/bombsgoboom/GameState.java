package edu.udel.jogordon.bombsgoboom;

import edu.udel.jogordon.gameframework.Position;
import edu.udel.jogordon.gameframework.State;
import java.util.ArrayDeque;


public class GameState implements State<GameState> {
	
	private PlayerState playerState;		// The player character 
	private ArrayDeque<Enemy> enemies;		// Mobile enemies that can injure/kill the player
	private ArrayDeque<Wall> walls;			// Stationary barricades that may or may not be destroyed depending on subclass
	private ArrayDeque<Powerup> powerups;	// Collectible objects that enhance the players statistics/abilities 
	private ArrayDeque<Bomb> bombs;			// Object that explodes killing EVERYTHING!!!!
	private ArrayDeque<Explosion> explosions; // The fireball from bombs (just for show)
	private GameTimer timer;				// Tracks how many seconds have passed since level began
	private int score;						// Player's score
	private int level;					    // Indicates the current level of difficulty
	private int rows;
	private int cols;
	
	public GameState(PlayerState playerState, ArrayDeque<Enemy> enemies, ArrayDeque<Wall> walls, 
			  		 ArrayDeque<Powerup> powerups, ArrayDeque<Bomb> bombs, ArrayDeque<Explosion> explosions, 
			  		 GameTimer timer, int score, int level, int rows, int cols) {
		this.playerState = playerState;
		this.enemies = enemies;
		this.walls = walls;
		this.powerups = powerups;
		this.bombs = bombs;
		this.explosions = explosions;
		this.timer = timer;
		this.score = score;
		this.level = level;
		this.rows = rows;
		this.cols = cols;

	}
	public PlayerState getPlayerState() {return playerState;}
	
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
	
	public ArrayDeque<Enemy> getEnemies() {return enemies;}
	
	public void setEnemies(ArrayDeque<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	public ArrayDeque<Wall> getWalls() {return walls;}
	
	public void setWalls(ArrayDeque<Wall> walls) {
		this.walls = walls;
	}
	
	public ArrayDeque<Powerup> getPowerups() {return powerups;}
	
	public void setPowerups(ArrayDeque<Powerup> powerups) {
		this.powerups = powerups;
	}
	public ArrayDeque<Bomb> getBombs() {return bombs;}
	
	public void setBombs(ArrayDeque<Bomb> bombs) {this.bombs = bombs;}
	
	public ArrayDeque<Explosion> getExplosions() {return explosions;}
	
	public void setExplosions(ArrayDeque<Explosion> explosions) {
		this.explosions = explosions;
	}
	public GameTimer getGameTimer() {
		return timer;
	}
	public void setGameTimer(GameTimer timer) {
		this.timer = timer;
	}
	public int getScore() {return score;}
	
	public void setScore(int score) {this.score = score;}
	
	public int getLevel() {return level;}
	
	public void setLevel(int level) {this.level = level;}
	
	public int getRows() {return rows;}
	
	public int getCols() {return cols;}
	
	public GameState copy() {
		ArrayDeque<Enemy> enemyCopy = new ArrayDeque<Enemy>();
		for(Enemy e : enemies) {
			enemyCopy.addLast(e.copy());	
			}
		ArrayDeque<Wall> wallCopy = new ArrayDeque<Wall>();
		for(Wall w : walls) {
			wallCopy.addLast(w.copy());	
			}
		ArrayDeque<Powerup> powerupCopy = new ArrayDeque<Powerup>();
		for(Powerup p : powerups) {
			powerupCopy.addLast(p.copy());	
			}
		ArrayDeque<Bomb> bombCopy = new ArrayDeque<Bomb>();
		for(Bomb b : bombs) {
			bombCopy.addLast(b.copy());	
		}
		ArrayDeque<Explosion> explosionCopy = new ArrayDeque<Explosion>();
		for(Explosion x : explosions) {
			explosionCopy.addLast(x.copy());
		}
		return new GameState(playerState.copy(), enemyCopy, wallCopy, powerupCopy, bombCopy, explosionCopy, timer, score, level, rows, cols);
	}
	
	public boolean isValid(Position p, Object obj) {
		//Makes sure move is inbounds and isn't stepping on anything it shouldn't
		
		boolean isWall = false;
		for(Wall w : walls) {
			if(p.equals(w.getPosition())) {isWall = true;}
		}
		boolean isBomb = false;
		for(Bomb b : bombs) {
			if(p.equals(b.getPosition())) {isBomb = true;}
		}
		boolean isEnemy = false;
		for(Enemy e : enemies) {
			if(p.equals(e.getPosition()) && !obj.equals(e) && !(obj instanceof PlayerState)) {
				isEnemy = true;
			}
		}
        return p.getRow() >= 0 && p.getRow() < rows && p.getCol() >= 0 && p.getCol() < cols &&
            !isWall && !isBomb && !isEnemy /*&& !p.equals(playerState.getPosition()*/;
    }

	public boolean isEnd() {
		//Checks to see if any endgame scenarios have taken place
		boolean isDead = false;
		boolean victory = false;
		//Killed by enemy?
		for(Enemy e : enemies) {
			if(e.getPosition().equals(playerState.getPosition())) {
				System.out.println("You were killed by an enemy!");
				isDead = true;
			}
		}
		//Killed by bomb?
		for(Bomb b : bombs) {
			if((b.getTimer() == 0) && ((b.getPosition().blockDistance(playerState.getPosition()) == 1) ||
				((b.getUpgrade() == "Power2x") && (b.getPosition().blockDistance(playerState.getPosition()) <= 2) && 
			   	((b.getPosition().getRow() == playerState.getPosition().getRow() || 
			   	b.getPosition().getCol() == playerState.getPosition().getCol()))))) {
				System.out.println("You were blown up!");
				isDead = true;
			}
		}
		//Won the game?
		if(enemies.isEmpty() && getLevel() == 3){
			victory = true;
		}
		return victory || isDead;
	}
	
	public String toString() {
        // build a grid first and then print the grid
        StringBuilder builder = new StringBuilder();
        builder.append("BombGoesBoom!:\n");
        builder.append("Level " + getLevel()).append("\n");
        builder.append("Score " + getScore()).append("\n");
        char[][] grid = new char[rows][cols]; 
        for(Powerup p : powerups) {
        	grid[p.getPosition().getRow()][p.getPosition().getCol()] = 'U'; // Place enemies on grid
        }  
        grid[playerState.getPosition().getRow()][playerState.getPosition().getCol()] = 'P'; //Place player on grid
        for(Wall w : walls) {
        	grid[w.getPosition().getRow()][w.getPosition().getCol()] = 'W'; // Place walls on grid
        } 
        for(Bomb b : bombs) {
        	grid[b.getPosition().getRow()][b.getPosition().getCol()] = 'B'; // Place bombs on grid
        }
        for(Enemy e : enemies) {
        	grid[e.getPosition().getRow()][e.getPosition().getCol()] = 'E'; // Place enemies on grid
        }
        for(Explosion x : explosions) {
        	grid[x.getPosition().getRow()][x.getPosition().getCol()] = 'X'; // Place explosions on grid
        }
 
        for (char[] row : grid) {
            for (char c : row) {
                if (c != 0) {
                    builder.append(c);
                }
                else {
                    builder.append(' ');
                }
            }
            builder.append('\n');
        }

        return builder.toString();    
	}
}
