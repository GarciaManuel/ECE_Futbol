package com.example.ece_futbol;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Player {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "assisted_hit")
    public int assistedHit;

    @ColumnInfo(name = "double_contact")
    public int doubleContact;

    @ColumnInfo(name = "catch_lift")
    public int catchLift;

    @ColumnInfo(name = "foot")
    public int foot;

    @ColumnInfo(name = "net_touch")
    public int netTouch;
}
