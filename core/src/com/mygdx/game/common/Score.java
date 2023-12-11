package com.mygdx.game.common;

public class Score {
    private final String playerName;
    private final int score;

    public Score(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public Score() {
        this.playerName = "";
        this.score = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}
