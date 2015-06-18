package edu.udel.jogordon.bombsgoboom.android;

import edu.udel.jogordon.gameframework.GameStateListener;

public class GameSoundManager extends SoundManager implements GameStateListener {
    private GameActivity activity;
    private int prevBombCount;
    public GameSoundManager(GameActivity context) {
        super(context);
        activity = context;
    }

    @Override
    public void onStateChange(Object game) {
        if (game == activity.getCurrentGame()) {
            int bombCount = activity.getCurrentGame().getCurrentState().getBombs().size();
            if (bombCount < prevBombCount) {
                playSound("Explosion");
                //http://soundbible.com/1468-Depth-Charge-Shorter.html
                System.out.print("Got in the if Statement");
            }
            prevBombCount = bombCount;
        }
        else {
            prevBombCount = 0;
        }
    }

}
