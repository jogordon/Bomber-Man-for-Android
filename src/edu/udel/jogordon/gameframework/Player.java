package edu.udel.jogordon.gameframework;

public interface Player<M extends Move<S>, S extends State<S>> {
    public M getNextMove(S state);
    public void toBombOrNotToBomb(S state);
    public String getIdentifier();
}
