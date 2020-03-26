package com.example.ece_futbol;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MatchGame.class, Team.class, Player.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MatchGameDao matchGameDao();
    public abstract TeamDao teamDao();
    public abstract PlayerDao playerDao();
}
