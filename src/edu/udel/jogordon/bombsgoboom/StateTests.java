package edu.udel.jogordon.bombsgoboom;

import java.util.ArrayDeque;
import java.util.Arrays;

import edu.udel.jogordon.gameframework.Position;

public class StateTests {
	static PlayerState player1 = new PlayerState(new Position(3, 3), 'D', new ArrayDeque<Position>());
	static ArrayDeque<Bomb> bombs1= new ArrayDeque<Bomb>();
	static ArrayDeque<Explosion> explosions1 = new ArrayDeque<Explosion>();
	static ArrayDeque<Wall> walls1 = new ArrayDeque<Wall>();
	static ArrayDeque<Enemy> enemies1 = new ArrayDeque<Enemy>();
	static ArrayDeque<Powerup> powerups1 = new ArrayDeque<Powerup>();
	
	static PlayerState player2 = new PlayerState(new Position(3, 3), 'R', new ArrayDeque<Position>());
	static ArrayDeque<Bomb> bombs2= new ArrayDeque<Bomb>();
	static ArrayDeque<Explosion> explosions2 = new ArrayDeque<Explosion>();
	static ArrayDeque<Wall> walls2 = new ArrayDeque<Wall>(Arrays.asList(new Wall(new Position(3, 8), 'U', false), new Wall(new Position(3, 8), 'U', false),
							 		 new Wall(new Position(4, 8), 'U', false), new Wall(new Position(6, 1), 'R', true), new Wall(new Position(6, 2), 'R', true), 
							 		 new Wall(new Position(6, 3), 'R', false)));
	static ArrayDeque<Enemy> enemies2 = new ArrayDeque<Enemy>(Arrays.asList(new Enemy(new Position(2, 6), 'L'), 
									    new Enemy(new Position(7, 8),'D'), new Enemy(new Position(4, 2), 'R')));
	static ArrayDeque<Powerup> powerups2 = new ArrayDeque<Powerup>(Arrays.asList(new Powerup(new Position(1, 13), 'U', "Power2x"), 
								 		   new Powerup(new Position(2, 7),'D', "Power2x"), new Powerup(new Position(4, 7), 'R', "Power2x")));
	
	static PlayerState player3 = new PlayerState(new Position(2, 2), 'L', new ArrayDeque<Position>());
	static ArrayDeque<Bomb> bombs3= new ArrayDeque<Bomb>();
	static ArrayDeque<Explosion> explosions3 = new ArrayDeque<Explosion>();
	static ArrayDeque<Wall> walls3 = new ArrayDeque<Wall>(Arrays.asList(new Wall(new Position(8, 8), 'L', false), new Wall(new Position(3, 3), 'L', false), 
									 new Wall(new Position(8, 1), 'U', false), new Wall(new Position(8, 2), 'D', true)));
	static ArrayDeque<Enemy> enemies3 = new ArrayDeque<Enemy>();
	static ArrayDeque<Powerup> powerups3 = new ArrayDeque<Powerup>(Arrays.asList(new Powerup(new Position(1, 13), 'R', "Power2x"), new Powerup(new Position(8, 7), 'D', "Power2x"),
										   new Powerup(new Position(8, 3), 'L', "Power2x")));
	
	public static GameState PREGAME = new GameState(player1, enemies1, walls1, powerups1, bombs1, explosions1, new GameTimer(), 0, 0, 10, 15);
		// Prior to the start of the game, no walls enemies or power-ups are deployed
	
	public static GameState INGAME = new GameState(player2, enemies2, walls2, powerups2, bombs2, explosions2, new GameTimer(), 20, 1, 10, 15);	
		/* During the game the player attempts to eliminate all enemies using bombs*/
	
	public static GameState ENDGAME = new GameState(player3, enemies3, walls3, powerups3, bombs3, explosions3, new GameTimer(), 122, 1, 10, 15);
		/* The game ends when no enemies remain on the board*/
	
    public static void main(String[] args) {
		System.out.println(PREGAME);
		System.out.println(INGAME);
		System.out.println(ENDGAME);
	}

}
