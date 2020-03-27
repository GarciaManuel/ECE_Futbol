package com.example.ece_futbol;

public class Team {
    int id;
    String name;
    String player1;
    String player2;
    int fourHits;
    int serviceOrder;
    int gameSets0;
    int gameSets1;
    int gameSets2;
    int gameSets3;
    int gameSets4;

    public Team(){}

    public Team(String name, String player1, String player2) {
        this.name = name;
        this.player1 = player1;
        this.player2 = player2;
        fourHits = 0;
        serviceOrder = 0;
        gameSets0 = 0;
        gameSets1 = 0;
        gameSets2 = 0;
        gameSets3 = 0;
        gameSets4 = 0;
    }

    // getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlayer1() {
       return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getFourHits() {
        return fourHits;
    }

    public int getServiceOrder() {
        return serviceOrder;
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

    public void setName(String value) {
        this.name = value;
    }

    public void setPlayer1(String value) {
        this.player1 = value;
    }

    public void setPlayer2(String value) {
        this.player2 = value;
    }

    public void setFourHits(int value) {
        this.fourHits = value;
    }

    public void setServiceOrder(int value) {
        this.serviceOrder = value;
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
