package edu.udel.jogordon.bombsgoboom.android;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import edu.udel.jogordon.gameframework.GameStateListener;
import edu.udel.jogordon.gameframework.Position;
import edu.udel.jogordon.bombsgoboom.Bomb;
import edu.udel.jogordon.bombsgoboom.Enemy;
import edu.udel.jogordon.bombsgoboom.Explosion;
import edu.udel.jogordon.bombsgoboom.PlayerState;
import edu.udel.jogordon.bombsgoboom.Powerup;
import edu.udel.jogordon.bombsgoboom.Wall;

public class GameView extends View implements GameStateListener {
    // the activity
    protected GameActivity activity;
    private Bitmap previous = getImageMap().get("soldierDN");
    
    //--- BEGIN - these properties can be saved so we don't need to recompute ----
    // the width and height of the current game view
    private int width;
    private int height;
    
    // the scale of the game board grid, how many pixels per col (x) and row (y)
    private float scale_x;
    private float scale_y;
    
    // the number of rows and columns in the current game
    private int rows;
    private int cols;
    //--- END -----
    
    // the loaded bitmaps of images used to draw the game
    private Map<String, Bitmap> imageMap;
    
      
    public GameView(GameActivity context) {
        super(context);
        activity = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundDrawable(new BitmapDrawable(getImageMap().get("grass")));

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPowerUps(canvas);
        
        drawWalls(canvas);
        
        drawEnemies(canvas);
             
        drawBombs(canvas);
        
        drawPlayer(canvas);
        
        drawExplosions(canvas);
    }
    
    /**
     * Loads images from the Android assets folder if they haven't been loaded
     * already.
     */
    Map<String, Bitmap> getImageMap() {
        if (imageMap == null) {
            imageMap = new HashMap<String, Bitmap>();
            try {
                String[] files = getContext().getAssets().list("images");
                
                for (String imageName : files) {
                    // Construct a BitMap from an asset
                    Bitmap bitmap = BitmapFactory.decodeStream(
                        getContext().getAssets().open("images/" + imageName));
                    imageMap.put(imageName.replaceFirst("\\..*", ""), bitmap);
                }
            }
            catch (IOException e) {
                Log.e("BombsGoBoom", "IOException", e);
            }
        }
        return imageMap;
    }
        
    // for performance only
    RectF rectF = new RectF();
    private void setRectFromPosition(Position position) {
        float left = scale_x * position.getCol();
        float top = scale_y * position.getRow();
        float right = left + scale_x;
        float bottom = top + scale_y;
        rectF.set(left, top, right, bottom);
    }
    
   	public void drawWalls(Canvas canvas) {
        Bitmap breakable = getImageMap().get("tree");
        Bitmap unbreakable = getImageMap().get("boulder");
        for(Wall w : activity.getCurrentGame().getCurrentState().getWalls()) {
        	setRectFromPosition(w.getPosition());
        	if(w.getBreakable()) {canvas.drawBitmap(breakable, null, rectF, null);}
        	else {canvas.drawBitmap(unbreakable, null, rectF, null);}
        }        
    }
    
    public void drawEnemies(Canvas canvas) {
        Bitmap enemyImage;
        for(Enemy e : activity.getCurrentGame().getCurrentState().getEnemies()) {
        	if(e.getOrientation() == 'U') {enemyImage = getImageMap().get("monsterUP");}
            else if(e.getOrientation() == 'D') {enemyImage = getImageMap().get("monsterDN");}
            else if(e.getOrientation() == 'R') {enemyImage = getImageMap().get("monsterRT");}	
            else {enemyImage = getImageMap().get("monsterLT");}
        	setRectFromPosition(e.getPosition());
        	canvas.drawBitmap(enemyImage, null, rectF, null);
        }
    }
    
    public void drawPowerUps(Canvas canvas) {
        Bitmap powerupImage;
        for(Powerup p : activity.getCurrentGame().getCurrentState().getPowerups()) {
        	powerupImage = getImageMap().get("tntcrate");
        	setRectFromPosition(p.getPosition());
        	canvas.drawBitmap(powerupImage, null, rectF, null);
        }
    }
    
    public void drawBombs(Canvas canvas) {
        Bitmap bombImage;
        for(Bomb b : activity.getCurrentGame().getCurrentState().getBombs()) {
        	if(b.getUpgrade() == "Power2x") {bombImage = getImageMap().get("bombU");}
            else {bombImage = getImageMap().get("bomb");}
        	setRectFromPosition(b.getPosition());
        	canvas.drawBitmap(bombImage, null, rectF, null);
        }
    }
    
    public void drawExplosions(Canvas canvas) {
        Bitmap explosionImage;
        for(Explosion x : activity.getCurrentGame().getCurrentState().getExplosions()) {
        	if(x.getOrientation() == 'C') {explosionImage = getImageMap().get("center");}
            else if(x.getOrientation() == 'D') {
            	if(isCenter(x.getPosition().getRow()-1, x.getPosition().getCol()) && x.getUpgrade() == "Power2x") {
            		explosionImage = getImageMap().get("innerDN");
            	}
            	else {explosionImage = getImageMap().get("outerDN");}
        	}
            else if(x.getOrientation() == 'R') {
            	if(isCenter(x.getPosition().getRow(), x.getPosition().getCol()-1) && x.getUpgrade() == "Power2x") {
            		explosionImage = getImageMap().get("innerRT");
            	}
            	else {explosionImage = getImageMap().get("outerRT");}
        	}
            else if (x.getOrientation() == 'L') {
            	if(isCenter(x.getPosition().getRow(), x.getPosition().getCol()+1) && x.getUpgrade() == "Power2x") {
            		explosionImage = getImageMap().get("innerLT");
            	}
            	else {explosionImage = getImageMap().get("outerLT");}
            }
            else {
            	if(isCenter(x.getPosition().getRow()+1, x.getPosition().getCol()) && x.getUpgrade() == "Power2x") {
            		explosionImage = getImageMap().get("innerUP");
            	}
            	else {explosionImage = getImageMap().get("outerUP");}
            }
        	
        	setRectFromPosition(x.getPosition());
        	canvas.drawBitmap(explosionImage, null, rectF, null);
        }
    }
    public boolean isCenter(int row, int col) {
    	Position isCenter = new Position(row, col);
    	for (Explosion x : activity.getCurrentGame().getCurrentState().getExplosions()) {
    		if (x.getPosition().equals(isCenter) && x.getOrientation() == 'C') {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void drawPlayer(Canvas canvas) {
        PlayerState player = activity.getCurrentGame().getCurrentState().getPlayerState();
        Bitmap image; 
        char orientation = player.getOrientation();
        if(orientation == 'U') {
        	image = getImageMap().get("soldierUP");
            previous = image;
        }
        else if(orientation == 'D') {
        	image = getImageMap().get("soldierDN");
            previous = image;
        }
        else if(orientation == 'R') {
        	image = getImageMap().get("soldierRT");
            previous = image;
        }	
        else if(orientation == 'L'){
        	image = getImageMap().get("soldierLT");
            previous = image;
        }
        else {image = previous;}
        
        setRectFromPosition(player.getPosition());        
        canvas.drawBitmap(image, null, rectF, null);
    }
    
    /**
     * This method is called by the Android platform when the app window size changes.
     * We store the initial setting of these so that we can compute the exact locations
     * to draw the components of our View.
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        
        updateScaling();
    }
    
	private void updateScaling() {
        if (activity.getCurrentGame() != null) {
            rows = activity.getCurrentGame().getCurrentState().getRows();
            cols = activity.getCurrentGame().getCurrentState().getCols();
            scale_x = (float)width / (float)cols;
            scale_y = (float)height / (float)rows;
        }
    }

    /**
     * Tell Android to re draw the View when the state changed.
     */
    public void onStateChange(Object game) {
        if (game == this.activity.getCurrentGame()) {
            invalidate();
        }
    }
}
