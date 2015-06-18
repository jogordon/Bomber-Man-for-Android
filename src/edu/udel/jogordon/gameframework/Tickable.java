package edu.udel.jogordon.gameframework;

public interface Tickable {
    public void onTick();
    public long getTickSpeed();
    public boolean isEnd();
}