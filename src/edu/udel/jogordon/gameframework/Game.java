package edu.udel.jogordon.gameframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Game<M extends Move<S>, S extends State<S>> implements Tickable {
    private S currentState;
    private List<GameStateListener> listeners;
    private Map<String, Player<M, S>> players;
    
    public Game(S currentState) {
        this.currentState = currentState;
        this.listeners = new ArrayList<GameStateListener>();
        this.players = new HashMap<String, Player<M, S>>();
    }
    
    public void addPlayer(Player<M, S> player) {
        players.put(player.getIdentifier(), player);
    }
    
    public void removePlayer(Player<M, S> player) {
        players.remove(player.getIdentifier());
    }
    
    public List<Player<M, S>> getPlayers() {
        return new ArrayList<Player<M, S>>(players.values());
    }
    
    public Player<M, S> getPlayer(String identifier) {
        return players.get(identifier);
    }

    public void addStateChangeListener(GameStateListener listener) {
        listeners.add(listener);
    }
    
    public void removeStateChangeListener(GameStateListener listener) {
        listeners.remove(listener);
    }
    
    protected void notifyStateChangeListeners() {
        for (GameStateListener listener : listeners) {
            listener.onStateChange(this);
        }
    }
    
    /**
     * Some games will want to return a specific player here if they are turn
     * based.  Other games can return null indicating that all players are
     * "current players" and can make a move now.
     * 
     * @return
     */
    protected Player<M, S> getCurrentPlayer() {
        return null;
    }
    
    /**
     * Some games will want to return a specific player here.  However,
     * not all games have winners.
     * 
     * @return
     */
    public Player<M, S> getWinner() {
        return null;
    }
    
    /**
     * Helper method that asks a given player to move.  Returns true
     * if an actual Move was made.  Checks the following:
     *  - if the game is ended don't allow a move
     *  - if the move is not valid for the current state don't allow
     */
    protected boolean askPlayerToMove(Player<M, S> player) {
        if (!isEnd()) {
            S state = getCurrentState();
            
            Move<S> move = player.getNextMove(state);
            if (move != null && move.isValid(state)) {          	
                move.make(state);
                player.toBombOrNotToBomb(state);
                return true;
            }
        }
        
        return false;
    }
    
    public void updatePlayerMoves() {
        if (!isEnd()) {
            Player<M, S> currentPlayer = getCurrentPlayer();
            boolean actionTaken = false;
            if (currentPlayer != null) {
                // ask the current player for its move
                actionTaken = askPlayerToMove(currentPlayer);
            }
            else {
                // no current player, so ask each player for a move
                for (Player<M, S> player : getPlayers()) {
                    // making sure to put actionTaken second so that
                    // we always call askPlayerToMove for each player
                    actionTaken = askPlayerToMove(player) || actionTaken;
                }
            }
            
            if (actionTaken) {
                notifyStateChangeListeners();
            }
        }
    }
    
    /**
     * By default the onTick will update the player moves. If you override
     * this method you should call updatePlayerMoves() or super.onTick().
     */
    public void onTick() {
   		updatePlayerMoves();
    }
    
    public long getTickSpeed() {
        // by default tick speed is 1000 which indicates one second per tick
        return 1000;
    }
    
    public S getCurrentState() {
        return currentState;
    }
    
    protected void setCurrentState(S state) {
        currentState = state;
    }

    public boolean isEnd() {
        return getCurrentState().isEnd();
    }
    
    public void start() {
        notifyStateChangeListeners();
    }
}
