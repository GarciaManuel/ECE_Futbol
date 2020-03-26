package com.example.ece_futbol;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Team {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "player1")
    public Player player1;

    @ColumnInfo(name = "player2")
    public Player player2;

    @ColumnInfo(name = "four_hits")
    public int fourHits;

    @ColumnInfo(name = "service_order")
    public int serviceOrder;

}
