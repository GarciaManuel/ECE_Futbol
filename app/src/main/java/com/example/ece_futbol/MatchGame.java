package com.example.ece_futbol;

//import Team.java;

public class MatchGame {
    int id;
    String teamAName;
    String teamBName;
    int gameSets0;
    int gameSets1;
    int gameSets2;
    int gameSets3;
    int gameSets4;

    public MatchGame(){}

    public MatchGame(int id, String teamA, String teamB) {
        this.id = id;
        gameSets0 = 2;
        gameSets1 = 2;
        gameSets2 = 2;
        gameSets3 = 2;
        gameSets4 = 2;
        this.teamAName = teamA;
        this.teamBName = teamB;
    }

    // getters
    public long getId() {
        return id;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public String getTeamBName() {
        return teamBName;
    }
    
    public int getGameSets0() {
        return gameSets0;
    }

    public int getGameSets1() {
        return gameSets1;
    }

    public int getGameSets2() {
        return gameSets2;
    }

    public int getGameSets3() {
        return gameSets3;
    }

    public int getGameSets4() {
        return gameSets4;
    } 
    
    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTeamAName(String value) {
        this.teamAName = value;
    }

    public void setTeamBName(String value) {
        this.teamBName = value;
    }
    
    public void setGameSets0(int value) {
        this.gameSets0 = value;
    }

    public void setGameSets1(int value) {
        this.gameSets1 = value;
    }

    public void setGameSets2(int value) {
        this.gameSets2 = value;
    }

    public void setGameSets3(int value) {
        this.gameSets3 = value;
    }

    public void setGameSets4(int value) {
        this.gameSets4 = value;
    }

}

