package edu.udel.jogordon.bombsgoboom;

public class GameRecord {
    private String player;
    private Integer score;
    private Long date;
    public GameRecord(String player, Integer score, Long date) {
        this.player = player;
        this.score = score;
        this.date = date;
    }
    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }
    public Integer getScore() {
        return score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }
    public Long getDate() {
        return date;
    }
    public void setDate(Long date) {
        this.date = date;
    }
    
}
