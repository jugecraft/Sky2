package com.sky;

public class PlayerStatistics {
    private int gamesWon;
    private int kills;

    public PlayerStatistics() {
        this.gamesWon = 0;
        this.kills = 0;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void incrementGamesWon() {
        this.gamesWon++;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void incrementKills() {
        this.kills++;
    }
}
