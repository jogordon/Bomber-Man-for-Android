package edu.udel.jogordon.gameframework;

public interface State<S extends State<S>> {
    public S copy();
    public boolean isEnd();
}
