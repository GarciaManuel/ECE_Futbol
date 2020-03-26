package com.example.ece_futbol;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM player WHERE name LIKE :name")
    Player findByName(String name);

    @Insert
    void insertAll(Player... players);
}
