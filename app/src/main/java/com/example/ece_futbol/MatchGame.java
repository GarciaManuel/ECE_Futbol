package com.example.ece_futbol;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MatchGame {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "teamA")
    public Team teamA;

    @ColumnInfo(name = "teamB")
    public Team teamB;

    @ColumnInfo(name = "game_sets")
    public int gameSets[];

    @ColumnInfo(name = "points")
    public int points[][];

}
