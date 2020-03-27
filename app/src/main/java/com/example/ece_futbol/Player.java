package com.example.ece_futbol;


public class Player {
    int id;
    String name;
    int assistedHit;
    int doubleContact;
    int catchLift;
    int foot;
    int netTouch;

    public Player(){}

    public Player(String name) {
        this.name = name;
        assistedHit = 0;
        doubleContact = 0;
        catchLift = 0;
        foot = 0;
        netTouch = 0;
    }

    // getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAssistedHit() {
        return assistedHit;
    }

    public int getDoubleContact() {
        return doubleContact;
    }

    public int getCatchLift() {
        return catchLift;
    }

    public int getFoot() {
        return foot;
    }

    public int getNetTouch() {
        return netTouch;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssistedHit(int assistedHit) {
        this.assistedHit = assistedHit;
    }

    public void setDoubleContact(int doubleContact) {
        this.doubleContact = doubleContact;
    }

    public void setCatchLift(int catchLift) {
        this.catchLift = catchLift;
    }

    public void setFoot(int foot) {
        this.foot = foot;
    }

    public void setNetTouch(int netTouch) {
        this.netTouch = netTouch;
    }
}
