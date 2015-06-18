package edu.udel.jogordon.bombsgoboom.android;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import edu.udel.jogordon.gameframework.Position;
import edu.udel.jogordon.bombsgoboom.BombsGoBoom;

public class MoveListener implements View.OnTouchListener, View.OnKeyListener {
    private GameActivity activity;
    private long oldClickTime;
    private long newClickTime;

    // the last position selected in game coordinates (could be either touched or keyed pressed)
    private Position moveToPosition;
    
    public MoveListener(GameActivity activity) {
        this.activity = activity;
    }

    public boolean onTouch(View v, MotionEvent event) {
    	oldClickTime = newClickTime;
    	newClickTime = System.currentTimeMillis();
        int action = event.getAction();
        BombsGoBoom game = activity.getCurrentGame();
        if (game != null) {
            int row = (int)((event.getY() / v.getHeight()) * 
                game.getCurrentState().getRows());
            int col = (int)((event.getX() / v.getWidth()) * 
                game.getCurrentState().getCols());
            if (action == MotionEvent.ACTION_DOWN) {
                makePlayerMove(new Position(row, col));
                }
        }
        
        // we don't need any more events in this sequence
        return false;
    }
    
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Position current = activity.getCurrentGame().getCurrentState().getPlayerState().getPosition();
        int row = current.getRow();
        int col = current.getCol();
        
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            row--;
        }
        else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            row++;
        }
        else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            col--;
        }
        else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            col++;
        }
        makePlayerMove(new Position(row, col));
        
        return false;
    }
    
    public Position getMoveToPosition() {
        return moveToPosition;
    }
   
    private void makePlayerMove(Position moveTo) {
        // forces a call to updatePlayerMoves when there is a moveToPosition
    	//System.out.println("Move Listener Activated");
        moveToPosition = moveTo;
        activity.getCurrentGame().updatePlayerMoves();
        moveToPosition = null;
    }
    public boolean isDoubleClick() {
    	//Calculate time between clicks to determine if a double click
    	return ((newClickTime - oldClickTime <= 500) && 
    			(newClickTime - oldClickTime > 0));
    }
}
