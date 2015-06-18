package edu.udel.jogordon.gameframework;

public interface Move<S extends State<S>> {
    public boolean isValid(S state);
    public void make(S state);
    public char getOrientation();
}
