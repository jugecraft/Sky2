package com.sky;

public class PlayerStatistics {
    private int gamesWon;
    private int kills;

    public void incrementGamesWon() {
        this.gamesWon++;
    }

    public void incrementKills() {
        this.kills++;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getKills() {
        return kills;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }
}