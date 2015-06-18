package edu.udel.jogordon.gameframework;

import java.util.Timer;
import java.util.TimerTask;


public class ConsoleTickManager {
    /**
     * This is an inner class definition.  We'll talk about these
     * later in the semester.
     */
    class Task extends TimerTask {
        public void run() {
            game.onTick();
            System.out.println(game.toString());
            if (game.isEnd()) {
                timer.cancel();
            }
            else {
                nextTask();
            }
        }
    }
    
    Tickable game;
    Timer timer;
    
    private ConsoleTickManager(Tickable game) {
        this.game = game;
        System.out.println(game.toString());

        timer = new Timer();
        nextTask();
    }
    
    void nextTask() {
        timer.schedule(new Task(), game.getTickSpeed());
    }
    
    public static final void manageGame(Tickable game) {
        new ConsoleTickManager(game);
    }
}
