package edu.udel.jogordon.bombsgoboom;

import java.util.ArrayDeque;
import java.util.Arrays;

import edu.udel.jogordon.gameframework.Position;
import junit.framework.TestCase;

public class StateTests2 extends TestCase {
	
	public static GameState INGAME = new GameState(
		new PlayerState(new Position(0, 0), 'D', new ArrayDeque<Position>(Arrays.asList(new Position(0,0)))),
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
    		new Powerup(new Position(2, 12), 'R', "Power2x"), new Powerup(new Position(6, 4), 'R', "Power2x"),
    		new Powerup(new Position(9, 0), 'R', "Power2x"), new Powerup(new Position(9, 10), 'R', "Power2x"))),
    	new ArrayDeque<Bomb>(), new ArrayDeque<Explosion>(), new GameTimer(), 0, 0, 10, 15); 
	
	public void test_onTick() {
		
		GameState testState = INGAME.copy();
		BombsGoBoom game = new BombsGoBoom(testState);
		game.onTick();
		Position enemyPos = game.getCurrentState().getEnemies().getLast().getPosition();
		assertEquals(1, enemyPos.blockDistance(INGAME.getEnemies().getLast().getPosition()));
		Position playerPos1 = game.getCurrentState().getPlayerState().getPosition();
		assertEquals(1, playerPos1.blockDistance(INGAME.getPlayerState().getPosition()));
		Position wallPos = game.getCurrentState().getWalls().getLast().getPosition();
		assertEquals(new Position(9, 8), wallPos);
		game.onTick();
		Position playerPos2 = game.getCurrentState().getPlayerState().getPosition();
		assertEquals(new Position(2, 0), playerPos2);
	}
    
}
