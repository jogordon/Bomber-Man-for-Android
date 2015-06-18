package edu.udel.jogordon.gameframework.android;

import android.os.Handler;
import edu.udel.jogordon.gameframework.Tickable;

public class AndroidTickManager implements Runnable {
    private Tickable game;
    private Handler timer;

    private AndroidTickManager(Tickable game) {
        this.game = game;
        timer = new Handler();
        timer.postDelayed(this, game.getTickSpeed());
    }
    
    public void run() {
        game.onTick();
        if (!game.isEnd()) {
            timer.postDelayed(this, game.getTickSpeed());
        }
    }
    
    public static final void manageGame(Tickable game) {
        new AndroidTickManager(game);
    }
}
