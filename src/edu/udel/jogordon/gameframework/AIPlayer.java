package edu.udel.jogordon.gameframework;

import java.util.ArrayList;
import java.util.List;

import edu.udel.jogordon.bombsgoboom.GameState;

public abstract class AIPlayer<M extends Move<S>, S extends State<S>> implements Player<M, S> {
    public abstract List<M> getAllValidMoves(S state);
    public abstract double getHeuristicScore(S state);
    
    /**
     * getNextMove uses a greedy algorithm to choose the best possible move. Steps:
     * 
     *  1. Generate a list of all valid moves.
     *  2. Initialize max score to Double.NEGATIVE_INFINITY and possible moves to an empty list.
     *  3. For each move:
     *     1) Copy the state to a new state.
     *     2) Make the move on the new state.
     *     3) Compute our heuristic score for the new state.
     *     4) If score > max score, set max score to score and clear possible move list.
     *     5) If score >= max score, add move to possible move list.
     *  4. Randomly choose a move from the list of possible moves that have max score.
     *  
     *  - if there are no possible valid moves this method should return null
     *  
     * @param state
     * @return
     */
    public M getNextMove(S state) {
        List<M> validMoves = getAllValidMoves(state);
        double max_score = Double.NEGATIVE_INFINITY;
        List<M> possibleMoves = new ArrayList<M>();
        for (M move : validMoves) {
            double score = getHeuristicScore(move, state);
            if (score > max_score) {
                max_score = score;
                possibleMoves.clear();
            }
            if (score >= max_score) {
                possibleMoves.add(move);
            }
        }
        if (possibleMoves.isEmpty()) {
            return null;
        }
        else {
            return possibleMoves.get((int)(Math.random()*possibleMoves.size()));
        }
    }
    
    /**
     * Allow AI subclasses to override this default behavior
     */
    protected double getHeuristicScore(M move, S state) {
        S newState = state.copy();
        move.make(newState);
        return getHeuristicScore(newState);
    }
    public abstract void toBombOrNotToBomb(GameState state);
    
    public String toString() {
        return getIdentifier();
    }
}
